package com.bcn.bmc.services;

import ch.qos.logback.classic.pattern.DateConverter;
import com.bcn.bmc.common.ConvertData;
import com.bcn.bmc.enums.ActiveStatus;
import com.bcn.bmc.models.*;
import com.bcn.bmc.repositories.OrganizationRepository;
import com.bcn.bmc.repositories.UserRepository;
import com.bcn.bmc.repositories.VerificationCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    private final OrganizationRepository organizationRepository;


    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,EmailService emailService,OrganizationRepository organizationRepository,
                                 AuthenticationManager authenticationManager, VerificationCodeRepository verificationCodeRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.verificationCodeRepository = verificationCodeRepository;
        this.organizationRepository = organizationRepository;
        this.emailService = emailService;
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
            user.setOrganization(request.getOrganization());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUserRole(request.getUserRole());
            user.setStatus(request.getStatus() != null ? request.getStatus() : ActiveStatus.ACTIVE);
            user.setNewUser(true);
            user.setAddress(request.getAddress());
            user = repository.save(user);

            String jwt = jwtService.generateToken(user);

            String code = generateVerificationCode();
            String createdCode =  createVerificationCode(code, user.getEmail(), user.getId(), "User Registration", com.bcn.bmc.enums.VerificationStatus.PENDING);

            if(!createdCode.isEmpty()){
                emailService.sendVerificationEmailToNewlyCreatedUser(user.getEmail(), user.getFirstName(), user.getLastName(), "Registration Successful",code, user.getUsername(),  request.getPassword());
            }

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


    public boolean IsFirstTimeLogin(User request) {
        User user = repository.findIsUserNew(request.getUsername(), request.getPassword());
        System.out.println("called isUserNew  getUsername(): " + user.getUsername());
        System.out.println("called isUserNew  isNewUser(): " + user.isNewUser());
        return user.isNewUser();
    }


    public int FirstTimeLoginValidated(String email, String password){
        return repository.setUserActive(email, password, false);


    }

    public AuthenticationResponse authenticate(User request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            User user = repository.findByUsername(request.getUsername()).orElseThrow();
            String jwt = jwtService.generateToken(user);
            if(user.getOrganization() == null){
                return new AuthenticationResponse(-1, null, "User Details are Incomplete. User Organization is not found");
            }
            if(IsFirstTimeLogin(user)){
                return new AuthenticationResponse(-1,"", "First Time Login", user.getEmail());
            }
//            return new AuthenticationResponse(-1,jwt, "User login was successful");
            Optional<Organization> userOrganization = organizationRepository.findOrganizationById(user.getOrganization());
           if(userOrganization.isEmpty() || userOrganization.get().getOrganizationName() == null){
               return new AuthenticationResponse(-1, null, "User Details are Incomplete. User Organization is not found");
           }else{
               return new AuthenticationResponse(user.getId(), jwt, "User login was successful", user.getEmail(), userOrganization.get(), user.getUserRole(), user.getFirstName(),  user.getLastName());
           }

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


    public VerificationStatus getEmailVerify(String email) {
        try {
            Optional<User> user = repository.findUserByEmail(email);
            return user.map(value -> new VerificationStatus(true, "email exist", value.getId())).orElseGet(() -> new VerificationStatus(false, "email not exist", -1));
        } catch (Exception e) {
            return new VerificationStatus(false, "something went wrong", -1);
        }
    }

    public String createVerificationCode(String code, String email, long userId, String purpose, com.bcn.bmc.enums.VerificationStatus status) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expireDateTime = currentDateTime.plusHours(24);
        VerificationCode newVerification = new VerificationCode(userId, email, code, currentDateTime, expireDateTime, purpose, status);
        VerificationCode savedVerificationCode = verificationCodeRepository.save(newVerification);
        return savedVerificationCode.getCode();
    }

    public VerificationStatus getVerificationCode(String email) {
        VerificationStatus verificationStatus = getEmailVerify(email);
        if (verificationStatus.isExist()) {
            String code = generateVerificationCode();
            String createdCode = createVerificationCode(code, email, verificationStatus.getUserid(), "forgot password", com.bcn.bmc.enums.VerificationStatus.PENDING);
            if (!createdCode.isEmpty()) {
                emailService.sendVerificationEmail(email,"Verification Code", createdCode);
                return new VerificationStatus(true, "A verification code has been sent to your email. Please check your inbox or spam folder.", -1);
            } else {
                return new VerificationStatus(false, "Verification Code Creation failed!", -1);
            }
        }
        return verificationStatus;
    }


    public VerificationStatus checkEmailAndVerificationCodeValid(String  Email, String code){
        VerificationCode verificationCode =  verificationCodeRepository.getDetailsForEmailAndCode(Email, code);
        if(verificationCode == null){
            return  new VerificationStatus(false, "Invalid Verification Code", -1);
        }
        else if(verificationCode.getStatus().equals(com.bcn.bmc.enums.VerificationStatus.PENDING) && LocalDateTime.now().isBefore(verificationCode.getExpiresAt())){
            return  new VerificationStatus(true, "Verification code validated", verificationCode.getUserId());
        }else{
            if(!verificationCode.getStatus().equals(com.bcn.bmc.enums.VerificationStatus.PENDING)){
                return  new VerificationStatus(false, "verification code is already used", verificationCode.getUserId());
            }else{
                return  new VerificationStatus(false, "verification code is Expired", verificationCode.getUserId());
            }
        }

    }

    public VerificationStatus verifyCode( String email, String code){
        if(!email.isEmpty() && !code.isEmpty()){
            return checkEmailAndVerificationCodeValid(email, code);
        }else{
            return new VerificationStatus(false, "Both Email and Verification code Required", -1);
        }

    }

    @Transactional
    public VerificationStatus resetPassword( String email, String code, String newPassword){
        if(!email.isEmpty() && !code.isEmpty() && !newPassword.isEmpty()){
            VerificationStatus verificationStatus = checkEmailAndVerificationCodeValid(email, code);
            if(verificationStatus.isExist()){
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encryptedPassword = passwordEncoder.encode(newPassword);
                int updatedRows =  repository.resetPasswordByUserId(verificationStatus.getUserid(), encryptedPassword, false);
                System.out.println("updatedRows = " + updatedRows);
                if (updatedRows > 0) {
                  int verificationUsed =  verificationCodeRepository.updateVerificationCode(com.bcn.bmc.enums.VerificationStatus.USED, email, code);
                    System.out.println("verificationUsed = " + verificationUsed);
                  if(FirstTimeLoginValidated(email, encryptedPassword) > 0) {
                      return new VerificationStatus(true, "Password reset successfully. Please log in using your new password.", verificationStatus.getUserid());
                  }else{
                      return new VerificationStatus(false, "Password reset failed due to a internal issue. please try again", -1);
                  }
                  } else {
                    return new VerificationStatus(false, "Password reset failed. Please try again and ensure you have entered the correct email and verification code sent to your email.", -1);
                }
            }else{
                return  new VerificationStatus(false, "Invalid Verification Code", -1);
            }
        }else{
            return new VerificationStatus(false, "Both Email and Verification code Required", -1);
        }

    }




    public String generateVerificationCode() {
        return RandomStringUtils.randomNumeric(6);
    }

}
