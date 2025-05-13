package com.swapapp.swapappmockserver.controller;

import com.swapapp.swapappmockserver.dto.User.LoginResponseDto;
import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.dto.User.UserLoginDto;
import com.swapapp.swapappmockserver.dto.User.UserRegisterDto;
import com.swapapp.swapappmockserver.repository.IUserRepository;
import com.swapapp.swapappmockserver.security.JwtUtil;
import com.swapapp.swapappmockserver.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

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
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null);
        }
    }



}
