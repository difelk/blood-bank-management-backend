package com.bcn.bmc.services;

import com.bcn.bmc.enums.FulfillmentStatus;
import com.bcn.bmc.enums.SendStatus;
import com.bcn.bmc.enums.Status;
import com.bcn.bmc.enums.TransactionType;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService {

    private final BloodRequestRepository bloodRequestRepository;

    private final BloodRequestDetailRepository bloodRequestDetailRepository;

    private final HospitalRepository hospitalRepository;

    private final StockSendRepository stockSendRepository;

    private final StockSendDetailsRepository stockSendDetailsRepository;

    private final StockRepository stockRepository;

    private final StockTransactionRepository stockTransactionRepository;


    public BloodRequestService(BloodRequestRepository bloodRequestRepository, BloodRequestDetailRepository bloodRequestDetailRepository, HospitalRepository hospitalRepository, StockSendRepository stockSendRepository, StockSendDetailsRepository stockSendDetailsRepository, StockRepository stockRepository, StockTransactionRepository stockTransactionRepository) {
        this.bloodRequestRepository = bloodRequestRepository;
        this.bloodRequestDetailRepository = bloodRequestDetailRepository;
        this.hospitalRepository = hospitalRepository;
        this.stockSendRepository = stockSendRepository;
        this.stockSendDetailsRepository = stockSendDetailsRepository;
        this.stockRepository = stockRepository;
        this.stockTransactionRepository = stockTransactionRepository;
    }


    @Transactional
    public CustomResponse requestBlood(UserAuthorize admin, List<HospitalStockRequest> hospitalsBloodRequest) {

        if (hospitalsBloodRequest.isEmpty()) {
            return new CustomResponse(-1, "Empty Request", "bcn-failed_empty", Status.FAILED);
        }

        try {
            for (HospitalStockRequest hospitalStockRequest : hospitalsBloodRequest) {
                System.out.println("setProviderOrganizationId - " + hospitalStockRequest.getHospitalId());
                BloodRequest bloodRequest = new BloodRequest();
                bloodRequest.setRequestDate(LocalDateTime.now());
                bloodRequest.setFulfillmentStatus(FulfillmentStatus.PENDING);
                bloodRequest.setRequestorOrganizationId(admin.getOrganization());
                bloodRequest.setProviderOrganizationId(hospitalStockRequest.getHospitalId());
                BloodRequest newRecord = bloodRequestRepository.save(bloodRequest);

                if (newRecord.getId() != null) {
                    saveBloodDetail(newRecord, "A +", hospitalStockRequest.getAPlus());
                    saveBloodDetail(newRecord, "A -", hospitalStockRequest.getAMinus());
                    saveBloodDetail(newRecord, "B +", hospitalStockRequest.getBPlus());
                    saveBloodDetail(newRecord, "B -", hospitalStockRequest.getBMinus());
                    saveBloodDetail(newRecord, "AB +", hospitalStockRequest.getABPlus());
                    saveBloodDetail(newRecord, "AB -", hospitalStockRequest.getABMinus());
                    saveBloodDetail(newRecord, "O+", hospitalStockRequest.getOPlus());
                    saveBloodDetail(newRecord, "O-", hospitalStockRequest.getOMinus());
                }
            }
        } catch (Exception e) {
            return new CustomResponse(-1, "Failed at Adding to Request", "bcn-failed_at_bloodRequestSave", Status.FAILED);
        }

        return new CustomResponse(0, "Request Successfully Created", "bcn-success_all", Status.SUCCESS);
    }

    private void saveBloodDetail(BloodRequest newRecord, String bloodType, String quantityStr) {
        if (quantityStr != null && !quantityStr.isEmpty()) {
            try {
                double quantity = Double.parseDouble(quantityStr);
                bloodRequestDetailRepository.save(new BloodRequestDetail(newRecord.getId(), bloodType, quantity));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid quantity format for blood type: " + bloodType);
            }
        }
    }


    public List<BloodRequestAllDetails> getAllRequestStock(UserAuthorize admin) {
        List<BloodRequestAllDetails> bloodRequestAllDetails = new ArrayList<>();

        try {
            List<BloodRequest> bloodRequests = bloodRequestRepository.findAllByOrgId(admin.getOrganization());

            if (!bloodRequests.isEmpty()) {
                for (BloodRequest bloodRequest : bloodRequests) {
                    Hospital hospital = hospitalRepository.findAllByOrganizationIdLong(bloodRequest.getProviderOrganizationId());

                    if (hospital == null) {
                        continue;
                    }

                    List<BloodRequestDetail> bloodRequestDetails = bloodRequestDetailRepository.findByBloodRequestId(bloodRequest.getId());
                    List<BloodKeyValue> bloodKeyValues = new ArrayList<>();
                    List<Long> requestedId = new ArrayList<>();

                    if (!bloodRequestDetails.isEmpty()) {
                        for (BloodRequestDetail bloodRequestDetail : bloodRequestDetails) {
                            bloodKeyValues.add(new BloodKeyValue(bloodRequestDetail.getId(), bloodRequestDetail.getBloodType(), bloodRequestDetail.getQuantity()));
                            requestedId.add(bloodRequestDetail.getBloodRequest());
                        }
                    }
                    List<StockSend> stockSend = stockSendRepository.findByRequestId(bloodRequest.getId());
                    LocalDateTime receiveDate = null;
                    if(!stockSend.isEmpty()){
                        System.out.println("stockSend.get(0).getSentDate(): " + stockSend.get(0).getSentDate());
                        receiveDate = stockSend.get(0).getSentDate();
                    }
                    System.out.println("stockSend length: " + stockSend.size());
                    bloodRequestAllDetails.add(new BloodRequestAllDetails(
                            bloodRequest.getId(),
                            requestedId,
                            new KeyValue(hospital.getId(), hospital.getHospitalName()),
                            bloodRequest.getFulfillmentStatus(),
                            bloodRequest.getRequestDate(),
                            receiveDate,
                            bloodKeyValues
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bloodRequestAllDetails;
    }


    public CustomResponse updateRequestDetails(UserAuthorize admin, List<BloodKeyValue> bloodKeyValues) {
        try {
            if (!bloodKeyValues.isEmpty()) {
                for (BloodKeyValue bloodKeyValue : bloodKeyValues) {
                    if (bloodKeyValue.getValue() == null) {
                        return new CustomResponse(-1, "Request Update Failed", "bcn-update_failed_value_null", Status.FAILED);
                    }
                    if (bloodKeyValue.getValue() == 0) {
                        BloodRequestDetail bloodRequestDetail = bloodRequestDetailRepository.getDetailsByIdAndBloodType(bloodKeyValue.getId(), bloodKeyValue.getKey());
                        if (bloodRequestDetail.getId() > 0) {
                            int effectedCount = bloodRequestRepository.deleteByRequestId(bloodRequestDetail.getBloodRequest(), FulfillmentStatus.CANCELED);
                            if (effectedCount > 0) {
                                return new CustomResponse(0, "Request Delete Successful", "bcn-delete-success", Status.SUCCESS);
                            } else {
                                return new CustomResponse(-1, "Request Delete Failed", "bcn-delete_failed_stock_request_id_not_match", Status.FAILED);
                            }
                        } else {
                            return new CustomResponse(-1, "Request Delete Failed", "bcn-delete_failed_stock_request_id_not_match", Status.FAILED);
                        }
                    } else if (bloodKeyValue.getValue() <= 0) {
                        return new CustomResponse(-1, "Request Update Failed", "bcn-update_failed_invalid_date", Status.FAILED);
                    } else {
                        int effectedRow = bloodRequestDetailRepository.updateDetailsByIdAndBloodType(bloodKeyValue.getId(), bloodKeyValue.getKey(), bloodKeyValue.getValue());
                    }
                }
                return new CustomResponse(0, "Request Update Successful", "bcn-update-success", Status.SUCCESS);
            }
            return new CustomResponse(-1, "Request Update Failed", "bcn-update_failed_empty_body", Status.FAILED);
        } catch (Exception e) {
            return new CustomResponse(-1, "Request Update Failed: " + e.getMessage(), "bcn-update_failed", Status.FAILED);
        }
    }

    public CustomResponse deleteStockRequestById(UserAuthorize admin, long id) {
        try {
            List<BloodRequestDetail> bloodRequestDetails = bloodRequestDetailRepository.findByBloodRequestId(id);

            if (bloodRequestDetails.isEmpty()) {
                return new CustomResponse(-1, "Request Delete Failed: No associated data found.", "bcn-delete_failed_no_data", Status.FAILED);
            }

            int affectedCount = bloodRequestRepository.deleteByRequestId(bloodRequestDetails.get(0).getBloodRequest(), FulfillmentStatus.CANCELED);

            if (affectedCount > 0) {
                return new CustomResponse(0, "Request Delete Successful", "bcn-delete-success", Status.SUCCESS);
            } else {
                return new CustomResponse(-1, "Request Delete Failed: Database error occurred.", "bcn-delete-failed_db_error", Status.FAILED);
            }

        } catch (Exception e) {
            return new CustomResponse(-1, "Request Delete Failed: " + e.getMessage(), "bcn-delete_failed", Status.FAILED);
        }
    }

    public List<BloodRequestAllDetails> getAllRequestStockAccordingToProvider(UserAuthorize admin) {

        System.out.println("inside the service");
        List<BloodRequestAllDetails> bloodRequestAllDetails = new ArrayList<>();

        try {
            List<BloodRequest> bloodRequests = bloodRequestRepository.findAllByProviderId(admin.getOrganization());

            if (!bloodRequests.isEmpty()) {
                for (BloodRequest bloodRequest : bloodRequests) {
                    Hospital hospital = hospitalRepository.findAllByOrganizationIdLong(bloodRequest.getRequestorOrganizationId());

                    if (hospital == null) {
                        continue;
                    }

                    List<BloodRequestDetail> bloodRequestDetails = bloodRequestDetailRepository.findByBloodRequestId(bloodRequest.getId());
                    List<BloodKeyValue> bloodKeyValues = new ArrayList<>();
                    List<Long> requestedId = new ArrayList<>();

                    if (!bloodRequestDetails.isEmpty()) {
                        for (BloodRequestDetail bloodRequestDetail : bloodRequestDetails) {
                            bloodKeyValues.add(new BloodKeyValue(bloodRequestDetail.getId(), bloodRequestDetail.getBloodType(), bloodRequestDetail.getQuantity()));
                            requestedId.add(bloodRequestDetail.getBloodRequest());
                        }
                    }

                    List<StockSend> stockSend = stockSendRepository.findByRequestId(bloodRequest.getId());
                    LocalDateTime receiveDate = null;
                    if(!stockSend.isEmpty()){
                        System.out.println("stockSend.get(0).getSentDate(): " + stockSend.get(0).getSentDate());
                        receiveDate = stockSend.get(0).getSentDate();
                    }
                    System.out.println("stockSend length: " + stockSend.size());
                    bloodRequestAllDetails.add(new BloodRequestAllDetails(
                            bloodRequest.getId(),
                            requestedId,
                            new KeyValue(hospital.getId(), hospital.getHospitalName()),
                            bloodRequest.getFulfillmentStatus(),
                            bloodRequest.getRequestDate(),
                            receiveDate,
                            bloodKeyValues
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bloodRequestAllDetails;
    }

    @Transactional
    public CustomResponse shareStock(UserAuthorize userAuthorize, BloodRequestAllDetails bloodRequestAllDetails) {
        try {
            StockSend stockSend = new StockSend();
            stockSend.setBloodRequestId(bloodRequestAllDetails.getRequestedId().get(0));
            stockSend.setSentByUserId(userAuthorize.getUserId());
            stockSend.setSentDate(LocalDateTime.now());
            stockSend.setStatus(SendStatus.SENT);
            System.out.println("pass 01");
            StockSend savedStockSend = stockSendRepository.save(stockSend);
            if (savedStockSend.getId() > 0) {
                System.out.println("pass 1.01");
                for (BloodKeyValue bloodKeyValue : bloodRequestAllDetails.getBloodGroups()) {
                    StockSendDetail stockSendDetail = new StockSendDetail();
                    stockSendDetail.setStockSend(savedStockSend.getId());
                    stockSendDetail.setBloodRequestDetail(bloodKeyValue.getId());
                    stockSendDetail.setSentQuantity(bloodKeyValue.getValue());
                    stockSendDetail.setStatus(SendStatus.SENT);
                    stockSendDetailsRepository.save(stockSendDetail);
                    Optional<Stock> stock = stockRepository.findByOrganizationIdAndBloodType(bloodRequestAllDetails.getHospital().getKey(), bloodKeyValue.getKey());
                    if (stock.isPresent()) {
                        int updatedStock = stockRepository.updateStockQuantityByOrganizationAndBloodType(bloodRequestAllDetails.getHospital().getKey(), bloodKeyValue.getKey(), stock.get().getQuantity() + bloodKeyValue.getValue());
                        stockTransactionRepository.save(new StockTransaction(stock.get().getId(), TransactionType.TRANSFER_IN, stock.get().getQuantity() + bloodKeyValue.getValue(), new Date(), null, bloodRequestAllDetails.getHospital().getKey(), (long) userAuthorize.getOrganization(), null));
                        System.out.println("pass 1.02");
                        if (updatedStock > 0) {
                            Optional<Stock> stock2 = stockRepository.findByOrganizationIdAndBloodType(userAuthorize.getOrganization(), bloodKeyValue.getKey());
                            if (stock2.isPresent()) {
                                int updatedStock2 = stockRepository.updateStockQuantityByOrganizationAndBloodType(userAuthorize.getOrganization(), bloodKeyValue.getKey(), stock2.get().getQuantity() - bloodKeyValue.getValue());
                                stockTransactionRepository.save(new StockTransaction(stock2.get().getId(), TransactionType.TRANSFER_OUT, stock2.get().getQuantity() - bloodKeyValue.getValue(), new Date(), null, (long) userAuthorize.getOrganization(), bloodRequestAllDetails.getHospital().getKey(), null));
                                System.out.println("pass 1.03");
                            } else {
                                System.out.println("pass 1.04");
                                return new CustomResponse(-1, "Stock send Failed: ", "bcn-stock_send_failed_stock2_missing", Status.FAILED);
                            }
                        } else {
                            System.out.println("pass 1.05");
                            return new CustomResponse(-1, "Stock send Failed: ", "bcn-stock_send_failed_at_update_stock_for_destination_org", Status.FAILED);
                        }
                    } else {
                        Stock stock1 = new Stock();
                        stock1.setOrganizationId(bloodRequestAllDetails.getRequestedId().get(0));
                        stock1.setBloodType(bloodKeyValue.getKey());
                        stock1.setQuantity(bloodKeyValue.getValue());
                        stock1.setLastUpdated(new Date());

                        Stock stockCreate = stockRepository.save(stock1);
                        System.out.println("pass 2");
                        if (stockCreate.getId() > 0) {
                            Optional<Stock> stock2 = stockRepository.findByOrganizationIdAndBloodType(userAuthorize.getOrganization(), bloodKeyValue.getKey());
                            if (stock2.isPresent()) {
                                int updatedStock2 = stockRepository.updateStockQuantityByOrganizationAndBloodType(userAuthorize.getOrganization(), bloodKeyValue.getKey(), stock2.get().getQuantity() - bloodKeyValue.getValue());
                                stockTransactionRepository.save(new StockTransaction(stockCreate.getId(), TransactionType.TRANSFER_IN, stock1.getQuantity(), new Date(), null, bloodRequestAllDetails.getHospital().getKey(), (long) userAuthorize.getOrganization(), null));
                                System.out.println("pass 2.01");
                            }
                        }
                    }
                }
                System.out.println("pass 3");
                int effectedRow =  bloodRequestRepository.updateStatus(bloodRequestAllDetails.getRequestedId().get(0), FulfillmentStatus.FULFILLED);

                return new CustomResponse(0, "Request Send Successful", "bcn-send-success", Status.SUCCESS);
            } else {
                System.out.println("pass 4");
                return new CustomResponse(-1, "Stock send Failed: ", "bcn-stock_send_failed_at_save_stock", Status.FAILED);
            }
        } catch (Exception e) {
            System.out.println("Stock send failed due to error: {}" + e.getMessage());
            return new CustomResponse(-1, "Stock send Failed: ", "bcn-stock_send_failed_at_save_stock", Status.FAILED);
        }
    }


}
