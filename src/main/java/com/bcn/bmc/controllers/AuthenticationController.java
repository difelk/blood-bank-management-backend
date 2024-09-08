package com.bcn.bmc.controllers;

import com.bcn.bmc.models.*;
import com.bcn.bmc.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {


    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody User request
    ) {
        System.out.println("Control REG: "+ request.getEmail());
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/login/email/{email}")
    public VerificationStatus getEmailVerify(@PathVariable String email){
        return authService.getEmailVerify(email);
    }


    @PostMapping("/login/validation-code")
    public VerificationStatus getVerificationCode(@RequestBody EmailRequest email) {
        System.out.println("getVerificationCode 1 : " + email.getEmail());
        return authService.getVerificationCode(email.getEmail());
    }

    @PostMapping("/login/verification-code")
    public VerificationStatus verifyCode(@RequestBody EmailRequest email) {
        return authService.verifyCode(email.getEmail(), email.getCode());
    }

    @PostMapping("/login/reset-password")
    public VerificationStatus resetPassword(@RequestBody PasswordReset details) {
        return authService.resetPassword(details.getEmail(), details.getCode(), details.getPassword());
    }

    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        boolean isValid = authService.isValidToken(token);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateTokenGet(@RequestParam("token") String token) {
        System.out.println("get token - " + token);
        boolean isValid = authService.isValidToken(token);
        return ResponseEntity.ok(isValid);
    }


}
