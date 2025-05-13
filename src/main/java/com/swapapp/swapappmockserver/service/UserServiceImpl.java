package com.swapapp.swapappmockserver.service;

import com.swapapp.swapappmockserver.dto.User.LoginResponseDto;
import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.dto.User.UserLoginDto;
import com.swapapp.swapappmockserver.dto.User.UserRegisterDto;
import com.swapapp.swapappmockserver.model.User;
import com.swapapp.swapappmockserver.repository.IUserRepository;
import com.swapapp.swapappmockserver.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public UserDto register(UserRegisterDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado.");
        }

        User newUser = new User(
                UUID.randomUUID().toString(),
                dto.getEmail(),
                dto.getFullName(),
                dto.getUsername(),
                dto.getPassword()
        );

        userRepository.save(newUser);
        return new UserDto(newUser.getEmail(), newUser.getFullName(), newUser.getUsername());
    }

    @Override
    public LoginResponseDto login(UserLoginDto dto) {
        return userRepository.findByEmail(dto.getEmail())
                .filter(user -> user.getPassword().equals(dto.getPassword()))
                .map(user -> new LoginResponseDto(
                        jwtUtil.generateToken(user.getEmail()),
                        user.getEmail(),
                        user.getFullName(),
                        user.getUsername()
                ))
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
    }

    @Override
    public UserDto getCurrentUser(String token) {
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email)
                .map(user -> new UserDto(user.getEmail(), user.getFullName(), user.getUsername()))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}
