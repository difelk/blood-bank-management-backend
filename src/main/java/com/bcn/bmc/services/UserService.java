package com.bcn.bmc.services;

import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.models.Address;
import com.bcn.bmc.models.Password;
import com.bcn.bmc.models.User;
import com.bcn.bmc.models.UserResponse;
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




    public User findUserByNic(String nic){
        try {
            Optional<User> user = userRepository.findUserByNic(nic);
            System.out.println("user - " + user);
            return user.orElse(null);
        } catch (Exception e) {
            System.out.println("Error finding user by NIC: " + e.getMessage());
            return null;
        }

    }


    public User findUserById(long id){
        try {
            Optional<User> user = userRepository.findUserById(id);
            System.out.println("user - " + user);
            if(user.isPresent()) {
                return user.get();
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error finding user by Id: " + e.getMessage());
            return null;
        }

    }

    public User getUserByUsername(String username) {
        try {
            Optional<User> user = userRepository.findUserByUsername(username);
            System.out.println("user - " + user);
            if(user.isPresent()) {
                return user.get();
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error finding user by NIC: " + e.getMessage());
            return null;
        }
    }

    public User getUsersByEmail(String email){
        try {
            Optional<User> user = userRepository.findUserByEmail(email);
            if(user.isPresent()) {
                return user.get();
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error finding user by Email: " + e.getMessage());
            return null;
        }
    }

    public UserResponse updateUser(User data){
        UserResponse userResponse = new UserResponse();
        User existingUser = findUserByNic(data.getNic());
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


    public UserResponse updatePassword(com.bcn.bmc.models.Password request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("inside of service method");
        UserResponse userResponse = new UserResponse();
        System.out.println("request.password = " + request.getNewPassword());
        Password password = new Password();
        password.setNic(request.getNic());
        password.setNewPassword(passwordEncoder.encode(request.getNewPassword()));
        try{
            userRepository.resetPassword(password.getNic(), password.getNewPassword(), 0);
            userResponse.setMessage("password reset Successful");
            userResponse.setStatus(200);
        }catch (Exception e){
            userResponse.setMessage("password reset Failed, with " + e.getMessage());
            userResponse.setStatus(200);
        }

        return userResponse;


    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAllActiveUsers();
        } catch (Exception e) {
            System.out.println("Failed to fetch all users: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public UserResponse deleteUserByNic(String nic) {
        UserResponse userResponse = new UserResponse();
        User user = findUserByNic(nic);
        if (user != null) {
            try {
                userRepository.deleteUserByNic(nic);
                User deletedUser = findUserByNic(nic);
                if (deletedUser == null) {
                    userResponse.setMessage("User delete successful");
                    userResponse.setStatus(200);
                } else {
                    userResponse.setMessage("Something went wrong");
                    userResponse.setStatus(500);
                }
            } catch (Exception e) {
                userResponse.setMessage("Failed to delete user: " + e.getMessage());
                userResponse.setStatus(500);
            }
        } else {
            userResponse.setMessage("User does not exist");
            userResponse.setStatus(404);
        }
        return userResponse;
    }


    @Transactional
    public UserResponse deleteUserByUserId(long id) {
        UserResponse userResponse = new UserResponse();
        User user = findUserById(id);
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
