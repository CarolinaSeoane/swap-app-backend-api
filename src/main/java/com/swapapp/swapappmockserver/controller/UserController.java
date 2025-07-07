package com.swapapp.swapappmockserver.controller;

import com.swapapp.swapappmockserver.config.AppProperties;
import com.swapapp.swapappmockserver.dto.User.ChangePasswordDto;
import com.swapapp.swapappmockserver.dto.User.LoginResponseDto;
import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.dto.User.UserLoginDto;
import com.swapapp.swapappmockserver.dto.User.UserRegisterDto;
import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import com.swapapp.swapappmockserver.service.user.UserServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.swapapp.swapappmockserver.exceptions.EmailNotFoundException;
import com.swapapp.swapappmockserver.exceptions.IncorrectPasswordException;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AppProperties appProperties;

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody UserRegisterDto dto) {
        try {
        System.out.println("Registering user: " + dto.getEmail());
            System.out.println("Full name: " + dto.getFullName());
            System.out.println("Username: " + dto.getUsername());
            
            LoginResponseDto response = userService.register(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error processing registration: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDto dto) {
        try {
            LoginResponseDto response = userService.login(dto);
            System.out.println("response login");
            System.out.println(response);
            return ResponseEntity.ok(response);
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error inesperado"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        System.out.println("getCurrentUser authHeader: " + authHeader);
        try {
            String token = authHeader.replace("Bearer ", "");
            UserDto user = userService.getCurrentUser(token);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody UserDto updatedUser
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            UserDto user = userService.updateProfile(token, updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
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
            System.out.println("imageUrl: " + imageUrl);
            return ResponseEntity.ok().body(Map.of("imageUrl", imageUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private UserDto addBaseUrlIfNeeded(UserDto user) {
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().startsWith("http")) {
            String baseUrl = appProperties.getBaseUrl();
            user.setProfileImageUrl(baseUrl + user.getProfileImageUrl());
        }
        return user;
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody ChangePasswordDto dto
    ) {
        System.out.println("Cambio de contraseña: " + dto.getCurrentPassword() + " -> " + dto.getNewPassword());
        try {
            String token = authHeader.replace("Bearer ", "");
            userService.changePassword(token, dto);
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/possible-trades")
    public ResponseEntity<?> possibleTrades(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            List<PossibleTrade> trades = userService.getPossibleTrades(token);
            return ResponseEntity.ok(trades);
        } catch (RuntimeException e) {
            System.out.println("Error getting possible trades: " + e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> updateCurrentUser(
            @PathVariable String email
    ) {
        try {
            UserDto user = userService.getUserByEmail(email);
            return ResponseEntity.ok(addBaseUrlIfNeeded(user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
