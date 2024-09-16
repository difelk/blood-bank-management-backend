package com.bcn.bmc.services;

import com.bcn.bmc.enums.BloodType;
import com.bcn.bmc.enums.FulfillmentStatus;
import com.bcn.bmc.enums.Status;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.BloodRequestDetailRepository;
import com.bcn.bmc.repositories.BloodRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BloodRequestService {

    private final BloodRequestRepository bloodRequestRepository;

    private final BloodRequestDetailRepository bloodRequestDetailRepository;

    public BloodRequestService(BloodRequestRepository bloodRequestRepository, BloodRequestDetailRepository bloodRequestDetailRepository) {
        this.bloodRequestRepository = bloodRequestRepository;
        this.bloodRequestDetailRepository = bloodRequestDetailRepository;
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


}
