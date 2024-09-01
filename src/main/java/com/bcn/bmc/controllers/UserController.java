package com.bcn.bmc.controllers;

import com.bcn.bmc.models.*;
import com.bcn.bmc.services.UserAddressService;
import com.bcn.bmc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserAddressService userAddressService;


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

    @DeleteMapping("/{nic}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> deleteUserByNic(@PathVariable String nic) {
        return ResponseEntity.ok(userService.deleteUserByNic(nic));
    }


//    address

    @PostMapping(path = "/address")
    public UserAddressResponse createAddress(@RequestBody Address address){
        System.out.println("called post add address");
        return userAddressService.createAddress(address);
    }


    @GetMapping("/address/{userid}")
    public Address getAddressByUser(@PathVariable Long userid){
        return userAddressService.getAddressByUserId(userid);
    }

}
