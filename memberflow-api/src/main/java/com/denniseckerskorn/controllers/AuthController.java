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

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Operations related to user authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

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
