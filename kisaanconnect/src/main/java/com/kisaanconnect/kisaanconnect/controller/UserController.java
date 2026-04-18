package com.kisaanconnect.kisaanconnect.controller;

import com.kisaanconnect.kisaanconnect.dto.CreateUserRequest;
import com.kisaanconnect.kisaanconnect.dto.CreateUserResponse;
import com.kisaanconnect.kisaanconnect.dto.LoginRequest;
import com.kisaanconnect.kisaanconnect.dto.LoginResponse;
import com.kisaanconnect.kisaanconnect.entity.User;
import com.kisaanconnect.kisaanconnect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //SignUp API
    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(
            @RequestBody @Valid CreateUserRequest request
    ) {
        CreateUserResponse response =userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    //Login Api
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }



}
