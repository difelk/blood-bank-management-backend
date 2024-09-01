package com.bcn.bmc.controllers;

import com.bcn.bmc.models.AuthenticationResponse;
import com.bcn.bmc.models.User;
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
        System.out.println("register - " + request.getDob());
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        System.out.println("post token - " + token);
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
