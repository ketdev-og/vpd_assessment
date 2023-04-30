package com.vpd.transaction.controllers;


import com.vpd.transaction.dto.requests.LoginRequest;
import com.vpd.transaction.dto.requests.RegisterRequest;
import com.vpd.transaction.dto.responses.LoginResponse;
import com.vpd.transaction.dto.responses.RegisterResponse;
import com.vpd.transaction.dto.responses.Response;
import com.vpd.transaction.exceptions.InvalidUserCredentialException;
import com.vpd.transaction.exceptions.NotFoundException;
import com.vpd.transaction.models.User;
import com.vpd.transaction.services.AuthService;
import com.vpd.transaction.services.JWTService;
import com.vpd.transaction.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = authService.registerUser(request);
            return Response.generateResponse(
                    "new user registered successfully",
                    HttpStatus.OK,
                    response
            );
        } catch (Exception e) {
            return Response.generateResponse(
                    e.getMessage(),
                    HttpStatus.MULTI_STATUS,
                    e.getCause().getCause().getMessage()
            );
        }

    }


    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.loginUser(loginRequest);
            return Response.generateResponse(
                    "auth token generated",
                    HttpStatus.OK,
                    response
            );

        } catch (BadCredentialsException e) {
            throw new InvalidUserCredentialException(e.getMessage());
        }
    }
}
