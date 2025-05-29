package com.denniseckerskorn.controllers;

import com.denniseckerskorn.dtos.LoginRequest;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.security.JwtUtil;
import com.denniseckerskorn.services.user_managment_services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController handles user authentication requests.
 * It provides an endpoint for user login, which returns a JWT token upon successful authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Operations related to user authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * Constructor for AuthController.
     *
     * @param authenticationManager Authentication manager for handling authentication requests.
     * @param jwtUtil               Utility class for generating JWT tokens.
     * @param userService           Service for handling user-related operations.
     */
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * API response codes for the login endpoint.
     *
     * @param loginRequest The login request containing user credentials.
     * @return ResponseEntity containing the JWT token or an error message.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @Operation(summary = "User login", description = "Authenticate a user and return a JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );

        User user = userService.findByEmail(loginRequest.getEmail());

        String authority = user.getRole().getPermissions().stream()
                .map(p -> p.getPermissionName().name())
                .findFirst()
                .orElse("VIEW_OWN_DATA");

        String token = jwtUtil.generateToken(user.getEmail(), authority);

        return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
    }
}
