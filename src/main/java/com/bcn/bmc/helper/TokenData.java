package com.bcn.bmc.helper;

import com.bcn.bmc.models.UserAuthorize;
import com.bcn.bmc.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenData {

    private final JwtService jwtService;
    private long userId;
    private int organizationId;
    private String userRole;

    @Autowired
    public TokenData(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public long getUserId() {
        return userId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public String getUserRole() {
        return userRole;
    }

    public UserAuthorize parseToken(String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            this.userId = jwtService.extractUserId(token);
            this.organizationId = jwtService.extractOrganizationId(token);
            this.userRole = jwtService.extractUserRole(token);
            return new UserAuthorize(userId, organizationId, userRole);
        } catch (Exception e) {
            System.out.println("Exception in Token Helper: " + e.getMessage());
            // Consider logging the exception or handling it in a more appropriate manner
            return null;
        }
    }
}
