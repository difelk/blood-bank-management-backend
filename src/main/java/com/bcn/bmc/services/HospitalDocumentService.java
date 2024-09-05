package com.bcn.bmc.services;

import com.bcn.bmc.models.Hospital;
import com.bcn.bmc.models.HospitalAddress;
import com.bcn.bmc.models.HospitalDocument;
import com.bcn.bmc.models.HospitalDocumentResponse;
import com.bcn.bmc.repositories.HospitalDocumentRepository;
import com.bcn.bmc.repositories.HospitalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalDocumentService {

    @Autowired
    private HospitalDocumentRepository hospitalDocumentRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Transactional
    public HospitalDocument storeFile(MultipartFile file, Long hospitalId) throws IOException {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid hospital ID"));

        HospitalDocument hospitalDocument = new HospitalDocument();
        hospitalDocument.setFileName(file.getOriginalFilename());
        hospitalDocument.setFileType(file.getContentType());
        hospitalDocument.setFileSize(file.getSize());
        hospitalDocument.setData(file.getBytes());
        hospitalDocument.setHospital(hospital);

        return hospitalDocumentRepository.save(hospitalDocument);
    }

    public List<HospitalDocument> getHospitalDocumentsById(Long hospitalId) {
        return hospitalDocumentRepository.findByHospitalId(hospitalId);
    }

    public List<HospitalDocument> getAllDocuments() {
        try {

            return hospitalDocumentRepository.findAll();
        } catch (Exception e) {
            System.out.println("Error fetching all documents: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public ResponseEntity<HospitalDocumentResponse> deleteDocumentById(Long documentId) {
        try {
            if (!hospitalDocumentRepository.existsById(documentId)) {
                return new ResponseEntity<>(
                        new HospitalDocumentResponse(null, null, null, null, "Document not found"),
                        HttpStatus.NOT_FOUND
                );
            }
            HospitalDocument document = hospitalDocumentRepository.findById(documentId).orElse(null);
            hospitalDocumentRepository.deleteById(documentId);

            return new ResponseEntity<>(
                    new HospitalDocumentResponse(
                            document.getId(),
                            document.getFileName(),
                            document.getFileType(),
                            document.getFileSize(),
                            "Document deleted successfully"
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new HospitalDocumentResponse(null, null, null, null, "Error occurred while deleting document"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}