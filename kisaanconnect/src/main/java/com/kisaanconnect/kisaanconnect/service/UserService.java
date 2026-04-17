package com.kisaanconnect.kisaanconnect.service;

import com.kisaanconnect.kisaanconnect.dto.CreateUserRequest;
import com.kisaanconnect.kisaanconnect.dto.CreateUserResponse;
import com.kisaanconnect.kisaanconnect.dto.LoginRequest;
import com.kisaanconnect.kisaanconnect.dto.LoginResponse;
import com.kisaanconnect.kisaanconnect.entity.User;
import com.kisaanconnect.kisaanconnect.exception.BusinessException;
import com.kisaanconnect.kisaanconnect.exception.ResourceNotFoundException;
import com.kisaanconnect.kisaanconnect.repository.UserRepository;
import com.kisaanconnect.kisaanconnect.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil=jwtUtil;
    }
    //Signup Logic
    public CreateUserResponse createUser(CreateUserRequest request) {
        User user=new User();
        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLatitude(request.getLatitude());
        user.setLongitude(request.getLongitude());
        User savedUser=userRepository.save(user);
        return new CreateUserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getPhoneNumber(),
                savedUser.getLatitude(),
                savedUser.getLongitude()
        );

    }

    //Login Logic
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getId());
        return new LoginResponse(token);
    }



}
