package com.bcn.bmc.services;

import com.bcn.bmc.models.User;
import com.bcn.bmc.models.UserDocument;
import com.bcn.bmc.repositories.UserDocumentRepository;
import com.bcn.bmc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserDocumentService {
    @Autowired
    private UserDocumentRepository userDocumentRepository;

    @Autowired
    private UserRepository userRepository;

    public UserDocument storeFile(MultipartFile file, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        UserDocument userDocument = new UserDocument();
        userDocument.setFileName(file.getOriginalFilename());
        userDocument.setFileType(file.getContentType());
        userDocument.setFileSize(file.getSize());
        userDocument.setData(file.getBytes());
        userDocument.setUser(user);

        return userDocumentRepository.save(userDocument);
    }


    public List<UserDocument> getUserDocumentsByUserId(Long userId) {
        return userDocumentRepository.findByUserId(userId);
    }
}
