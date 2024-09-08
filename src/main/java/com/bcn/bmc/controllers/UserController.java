package com.bcn.bmc.controllers;

import com.bcn.bmc.helper.TokenData;
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

    @Autowired
    private TokenData tokenHelper;
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getAllUsers(@RequestHeader("Authorization") String tokenHeader){
        UserAuthorize userAuthorize =   tokenHelper.parseToken(tokenHeader);
        return userService.getAllUsers(userAuthorize);
    }


    @GetMapping("/nic/{nic}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getAllUsersByNic(@RequestHeader("Authorization") String tokenHeader, @PathVariable String nic){
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return userService.findUserByNic(nic, userAuthorize);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getUserById(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long id){
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return userService.findUserById(id, userAuthorize);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getUsersByUserName(@RequestHeader("Authorization") String tokenHeader, @PathVariable String username){
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return userService.getUserByUsername(username, userAuthorize);
    }


    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getUsersByEmail(@RequestHeader("Authorization") String tokenHeader, @PathVariable String email){
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return userService.getUsersByEmail(email, userAuthorize);
    }


    @PutMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@RequestHeader("Authorization") String tokenHeader, @RequestBody User data){
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return  ResponseEntity.ok(userService.updateUser(data, userAuthorize));
    }


    @PutMapping("/password")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserResponse> updatePassword(@RequestHeader("Authorization") String tokenHeader, @RequestBody Password request){
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(userService.updatePassword(request, userAuthorize));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> deleteUserByUserId(@RequestHeader("Authorization") String tokenHeader, @PathVariable long id) {
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return ResponseEntity.ok(userService.deleteUserByUserId(id, userAuthorize));
    }


//    address

    @PostMapping(path = "/address")
    public UserAddressResponse createAddress(@RequestHeader("Authorization") String tokenHeader, @RequestBody Address address){
        System.out.println("called post add address");
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return userAddressService.createAddress(address);
    }

    @PutMapping("/address")
// @PreAuthorize("hasAuthority('ROLE_USER')")
    public UserAddressResponse updateAddress(@RequestHeader("Authorization") String tokenHeader, @RequestBody Address address) {
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
        return userAddressService.updateAddress(address);
    }

    @GetMapping("/address/{userid}")
    public Address getAddressByUser(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long userid){
        UserAuthorize userAuthorize =  tokenHelper.parseToken(tokenHeader);
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
