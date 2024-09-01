package com.bcn.bmc.services;

import ch.qos.logback.classic.pattern.DateConverter;
import com.bcn.bmc.common.ConvertData;
import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.models.AuthenticationResponse;
import com.bcn.bmc.models.User;
import com.bcn.bmc.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponse register(User request) {
        try {
            if (repository.findByUsername(request.getUsername()).isPresent()) {
                return new AuthenticationResponse(-1,null, "User already exists");
            }

            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setUsername(request.getUsername());
            user.setNic(request.getNic());
            user.setContactNumber(request.getContactNumber());
            user.setContactNumber2(request.getContactNumber2());
            user.setBloodType(request.getBloodType());
            user.setEmail(request.getEmail());
            user.setDob(request.getDob());
//            user.setOrganizationType(request.getOrganizationType());
            user.setOrganization(request.getOrganization());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUserRole(request.getUserRole());
            user.setStatus(request.getStatus() != null ? request.getStatus() : ActiveStatus.ACTIVE);
            user.setNewUser(true);
            user.setAddress(request.getAddress());
            user.setDocuments(request.getDocuments());

            user = repository.save(user);

            String jwt = jwtService.generateToken(user);

            return new AuthenticationResponse(user.getId(),jwt, "User registration was successful");
        } catch(DataIntegrityViolationException e) {
            System.out.println("error reg: " + e.getMessage());
            return new AuthenticationResponse(-1,null, "ERROR: Username already exists");
        } catch (InvalidDataAccessApiUsageException e) {
            return new AuthenticationResponse(-1,null, "ERROR: Invalid data access API usage");
        } catch (TransactionSystemException e) {
            return new AuthenticationResponse(-1,null, "ERROR: Transaction system exception");
        } catch (PersistenceException e) {
            return new AuthenticationResponse(-1,null, "ERROR: Persistence exception");
        } catch (Exception e) {
            return new AuthenticationResponse(-1,null, "User registration failed: " + e.getMessage());
        }
    }


    public AuthenticationResponse authenticate(User request) {
        System.out.println("auth res username: " + request.getUsername());
        System.out.println("auth res password: " + request.getPassword());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            System.out.println("checking username validation...");
            User user = repository.findByUsername(request.getUsername()).orElseThrow();
            System.out.println("checking username validation... : " + user.getUsername());
            String jwt = jwtService.generateToken(user);
            return new AuthenticationResponse(-1,jwt, "User login was successful");
        } catch (AuthenticationException e) {
            System.out.println("error: " + e.getMessage());
            return new AuthenticationResponse(-1,null, "Incorrect username or password. Please try again.");
        } catch (EntityNotFoundException e) {
            return new AuthenticationResponse(-1,null, "User not found.");
        } catch (Exception e) {
            return new AuthenticationResponse(-1,null, "Error during authentication: " + e.getMessage());
        }

    }

    public boolean isValidToken(String token) {
        System.out.println("isValidToken  in auth service: " + token);
        return jwtService.isValidToken(token);
    }


}
