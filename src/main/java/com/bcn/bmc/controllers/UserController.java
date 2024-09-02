package com.bcn.bmc.controllers;

import com.bcn.bmc.models.*;
import com.bcn.bmc.services.UserAddressService;
import com.bcn.bmc.services.UserDocumentService;
import com.bcn.bmc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserAddressService userAddressService;

    @Autowired
    private UserDocumentService userDocumentService;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }


    @GetMapping("/nic/{nic}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getAllUsersByNic(@PathVariable String nic){
        return userService.findUserByNic(nic);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getUsersByUserName(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@RequestBody User data){
        return  ResponseEntity.ok(userService.updateUser(data));
    }

    @PutMapping("/password")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserResponse> updatePassword(@RequestBody Password request){
        return ResponseEntity.ok(userService.updatePassword(request));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> deleteUserByUserId(@PathVariable long id) {
        return ResponseEntity.ok(userService.deleteUserByUserId(id));
    }


//    address

    @PostMapping(path = "/address")
    public UserAddressResponse createAddress(@RequestBody Address address){
        System.out.println("called post add address");
        return userAddressService.createAddress(address);
    }

    @PutMapping("/address")
// @PreAuthorize("hasAuthority('ROLE_USER')")
    public UserAddressResponse updateAddress(@RequestBody Address address) {
        return userAddressService.updateAddress(address);
    }

    @GetMapping("/address/{userid}")
    public Address getAddressByUser(@PathVariable Long userid){
        return userAddressService.getAddressByUserId(userid);
    }

//    documents

    @PostMapping("/documents/upload")
    public ResponseEntity<UserDocument> uploadFile(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("userId") Long userId) {
        try {
            UserDocument savedDocument = userDocumentService.storeFile(file, userId);
            return new ResponseEntity<>(savedDocument, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/documents/{userId}")
    public ResponseEntity<List<UserDocumentResponse>> getUserDocuments(@PathVariable Long userId) {
        List<UserDocument> userDocuments = userDocumentService.getUserDocumentsByUserId(userId);

        List<UserDocumentResponse> response = userDocuments.stream().map(doc -> new UserDocumentResponse(
                doc.getId(),
                doc.getFileName(),
                doc.getFileType(),
                doc.getFileSize(),
                java.util.Base64.getEncoder().encodeToString(doc.getData())
        )).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/documents/{documentId}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDocumentResponse> deleteDocumentById(@PathVariable long documentId) {
        return userDocumentService.deleteDocumentById(documentId);
    }

}
