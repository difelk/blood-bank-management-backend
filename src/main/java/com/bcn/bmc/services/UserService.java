package com.bcn.bmc.services;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.helper.TokenData;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.UserAddressRepository;
import com.bcn.bmc.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




    public User findUserByNic(String nic, UserAuthorize admin){
        try {
            Optional<User> user;
            if((admin.getOrganization() == 1)){
                user = userRepository.findUserByNic(nic);
            }else{
                user = userRepository.findUserByNicAndLoggedUserOrganization(admin.getOrganization(), nic);
                System.out.println("user - " + user);
            }
            return user.orElse(null);
        } catch (Exception e) {
            System.out.println("Error finding user by NIC: " + e.getMessage());
            return null;
        }

    }


    public User findUserById(long id, UserAuthorize admin){
        try {
            Optional<User> user;
            if((admin.getOrganization() == 1)){
                user  = userRepository.findUserById(id);
            }else{
                user  = userRepository.findUserByIdAndLoggedUserOrganization(admin.getOrganization(),id);
            }
            System.out.println("user - " + user);
            return user.orElse(null);
        } catch (Exception e) {
            System.out.println("Error finding user by Id: " + e.getMessage());
            return null;
        }

    }

    public User getUserByUsername(String username, UserAuthorize admin) {
        try {
            Optional<User> user;

            if((admin.getOrganization() == 1)){
                user = userRepository.findUserByUsername(username);
            }else{
                user  = userRepository.findUserByUsernameAndLoggedUserOrganization(admin.getOrganization(),username);
            }
            System.out.println("user - " + user);
            return user.orElse(null);
        } catch (Exception e) {
            System.out.println("Error finding user by NIC: " + e.getMessage());
            return null;
        }
    }

    public User getUsersByEmail(String email, UserAuthorize admin){
        try {
            Optional<User> user;

            if((admin.getOrganization() == 1)){
                user = userRepository.findUserByEmail(email);
            }else{
                user  = userRepository.findUserByEmailAndLoggedUserOrganization(admin.getOrganization(),email);
            }
            return user.orElse(null);
        } catch (Exception e) {
            System.out.println("Error finding user by Email: " + e.getMessage());
            return null;
        }
    }

    public UserResponse updateUser(User data, UserAuthorize admin){
        UserResponse userResponse = new UserResponse();
        User existingUser = findUserByNic(data.getNic(), admin);
        try {
            if (existingUser != null) {
                existingUser.setFirstName(data.getFirstName());
                existingUser.setLastName(data.getLastName());
                existingUser.setNic(data.getNic());
                existingUser.setUsername(data.getUsername());
                existingUser.setDob(data.getDob());
                existingUser.setBloodType(data.getBloodType());
                existingUser.setContactNumber(data.getContactNumber());
                existingUser.setAddress(data.getAddress());
//                existingUser.setStreet(data.getStreet());
//                existingUser.setCity(data.getCity());
                existingUser.setOrganization(data.getOrganization());
//                existingUser.setOrganizationType(data.getOrganizationType());
                existingUser.setPassword(data.getPassword());
                existingUser.setUserRole(data.getUserRole());

                userRepository.save(existingUser);
                userResponse.setMessage("user update successful");
                userResponse.setStatus(200);
            } else {
                userResponse.setMessage("user does not exist");
                userResponse.setStatus(404);
            }
        } catch(Exception e){
            userResponse.setMessage("ERROR: " + e.getMessage());
            userResponse.setStatus(500);
        }
        return userResponse;
    }


    public UserResponse updatePassword(com.bcn.bmc.models.Password request, UserAuthorize admin){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserResponse userResponse = new UserResponse();
        Password password = new Password();
        password.setNic(request.getNic());
        password.setNewPassword(passwordEncoder.encode(request.getNewPassword()));
        try{
            if((admin.getOrganization() == 1)){
                userRepository.resetPassword(password.getNic(), password.getNewPassword(), 0);
            }else{
                userRepository.resetPasswordByLoggedUserOrganization(admin.getOrganization(), password.getNic(), password.getNewPassword(), 0);
            }
            userRepository.resetPassword(password.getNic(), password.getNewPassword(), 0);
            userResponse.setMessage("password reset Successful");
            userResponse.setStatus(200);
        }catch (Exception e){
            userResponse.setMessage("password reset Failed, with " + e.getMessage());
            userResponse.setStatus(200);
        }

        return userResponse;


    }

    public List<User> getAllUsers(UserAuthorize admin) {
        try {
            System.out.println("User org: "+ admin.getOrganization());
            if(admin.getOrganization() == 1){
                return userRepository.findAllActiveUsers();
            }else{
                return userRepository.findAllActiveUsersByOrganization(admin.getOrganization());
            }

        } catch (Exception e) {
            System.out.println("Failed to fetch all users: " + e.getMessage());
            return null;
        }
    }

//    @Transactional
//    public UserResponse deleteUserByNic(String nic) {
//        UserResponse userResponse = new UserResponse();
//        User user = findUserByNic(nic);
//        if (user != null) {
//            try {
//                userRepository.deleteUserByNic(nic);
//                User deletedUser = findUserByNic(nic);
//                if (deletedUser == null) {
//                    userResponse.setMessage("User delete successful");
//                    userResponse.setStatus(200);
//                } else {
//                    userResponse.setMessage("Something went wrong");
//                    userResponse.setStatus(500);
//                }
//            } catch (Exception e) {
//                userResponse.setMessage("Failed to delete user: " + e.getMessage());
//                userResponse.setStatus(500);
//            }
//        } else {
//            userResponse.setMessage("User does not exist");
//            userResponse.setStatus(404);
//        }
//        return userResponse;
//    }


    @Transactional
    public UserResponse deleteUserByUserId(long id, UserAuthorize admin) {
        UserResponse userResponse = new UserResponse();
        User user = findUserById(id, admin);
        if (user != null) {
            try {
                user.setStatus(ActiveStatus.INACTIVE);
                userRepository.save(user);
                userResponse.setMessage("User has been marked as deleted");
                userResponse.setStatus(200);
            } catch (Exception e) {
                userResponse.setMessage("Failed to mark user as deleted: " + e.getMessage());
                userResponse.setStatus(500);
            }
        } else {
            userResponse.setMessage("User does not exist");
            userResponse.setStatus(404);
        }
        return userResponse;
    }


}
