package com.bcn.bmc.services;

import com.bcn.bmc.enums.BloodType;
import com.bcn.bmc.enums.FulfillmentStatus;
import com.bcn.bmc.enums.Status;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.BloodRequestDetailRepository;
import com.bcn.bmc.repositories.BloodRequestRepository;
import com.bcn.bmc.repositories.HospitalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BloodRequestService {

    private final BloodRequestRepository bloodRequestRepository;

    private final BloodRequestDetailRepository bloodRequestDetailRepository;

    private final HospitalRepository hospitalRepository;

    public BloodRequestService(BloodRequestRepository bloodRequestRepository, BloodRequestDetailRepository bloodRequestDetailRepository, HospitalRepository hospitalRepository) {
        this.bloodRequestRepository = bloodRequestRepository;
        this.bloodRequestDetailRepository = bloodRequestDetailRepository;
        this.hospitalRepository = hospitalRepository;
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
                    bloodRequestAllDetails.add(new BloodRequestAllDetails(
                            bloodRequest.getId(),
                            requestedId,
                            new KeyValue(hospital.getId(), hospital.getHospitalName()),
                            bloodRequest.getFulfillmentStatus(),
                            bloodRequest.getRequestDate(),
                            null,
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


}
