package com.swapapp.swapappmockserver.controller;

import com.swapapp.swapappmockserver.config.AppProperties;
import com.swapapp.swapappmockserver.dto.User.LoginResponseDto;
import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.dto.User.UserLoginDto;
import com.swapapp.swapappmockserver.dto.User.UserRegisterDto;
import com.swapapp.swapappmockserver.repository.IUserRepository;
import com.swapapp.swapappmockserver.security.JwtUtil;
import com.swapapp.swapappmockserver.service.UserServiceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AppProperties appProperties;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegisterDto dto) {
        UserDto registered = userService.register(dto);
        return ResponseEntity.ok(registered);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserLoginDto dto) {
        try {
            LoginResponseDto response = userService.login(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            UserDto user = userService.getCurrentUser(token);

            if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().startsWith("http")) {
                String baseUrl = appProperties.getBaseUrl();
                user.setProfileImageUrl(baseUrl + user.getProfileImageUrl());

            }

            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("/upload-profile-image")
    public ResponseEntity<?> uploadProfileImage(
        @RequestParam("image") MultipartFile image,
        @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String imageUrl = userService.saveProfileImage(token, image);
            String baseUrl = appProperties.getBaseUrl();
            String fullUrl = baseUrl + imageUrl;
            return ResponseEntity.ok().body(Map.of("imageUrl", fullUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
