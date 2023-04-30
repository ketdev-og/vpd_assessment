package com.vpd.transaction.services;


import com.vpd.transaction.dto.requests.LoginRequest;
import com.vpd.transaction.dto.requests.RegisterRequest;
import com.vpd.transaction.dto.responses.LoginResponse;
import com.vpd.transaction.dto.responses.RegisterResponse;
import com.vpd.transaction.dto.responses.Response;
import com.vpd.transaction.exceptions.NotFoundException;
import com.vpd.transaction.models.User;
import com.vpd.transaction.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    JWTService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder encoder;

    public RegisterResponse registerUser(RegisterRequest request){
            User user = User.builder()
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(encoder.encode(request.getPassword()))
                    .build();

            User newUser = userRepo.save(user);

        return RegisterResponse
                .builder()
                .userEmail(newUser.getEmail())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .build();
    }

    public LoginResponse loginUser (LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User usersDetails = (User) authentication.getPrincipal();

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authentication.getName());
            return LoginResponse.builder()
                    .authEmail(usersDetails.getEmail())
                    .authToken(token)
                    .build();
        }

        return null;
    }
}
