package com.swapapp.swapappmockserver.service.user;

import com.swapapp.swapappmockserver.dto.User.ChangePasswordDto;
import com.swapapp.swapappmockserver.dto.User.LoginResponseDto;
import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.dto.User.UserLoginDto;
import com.swapapp.swapappmockserver.dto.User.UserRegisterDto;
import com.swapapp.swapappmockserver.model.User;
import com.swapapp.swapappmockserver.repository.user.IUserRepository;
import com.swapapp.swapappmockserver.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.swapapp.swapappmockserver.exceptions.EmailNotFoundException;
import com.swapapp.swapappmockserver.exceptions.IncorrectPasswordException;
import java.util.UUID;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto register(UserRegisterDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado.");
        }

        User newUser = new User(
            UUID.randomUUID().toString(),
            dto.getEmail(),
            dto.getFullName(),
            dto.getUsername(),
            passwordEncoder.encode(dto.getPassword()),
            dto.getProfileImageUrl()
        );

        userRepository.save(newUser);

        String token = jwtUtil.generateToken(newUser.getEmail());

        return new LoginResponseDto(
            token,
            newUser.getEmail(),
            newUser.getFullName(),
            newUser.getUsername()
        );
    }


    @Override
    public LoginResponseDto login(UserLoginDto dto) {
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());

        if (optionalUser.isEmpty()) {
            throw new EmailNotFoundException("El email no está registrado");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Contraseña incorrecta");
        }

        return new LoginResponseDto(
                jwtUtil.generateToken(user.getEmail()),
                user.getEmail(),
                user.getFullName(),
                user.getUsername()
        );
    }


    @Override
    public UserDto getCurrentUser(String token) {
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email)
                .map(user -> new UserDto(user.getEmail(), user.getFullName(), user.getUsername(), user.getProfileImageUrl()))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public String saveProfileImage(String token, MultipartFile image) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String filename = UUID.randomUUID() + "-" + image.getOriginalFilename();
        String uploadDir = "uploads/profile-images";

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File dest = new File(uploadPath, filename);
    try {
            image.transferTo(dest);
        } catch (IOException | IllegalStateException e) {
            throw new RuntimeException("Error al guardar la imagen de perfil", e);
        }

        String imageUrl = "/images/" + filename;
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    @Override
    public UserDto updateProfile(String token, UserDto dto) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        userRepository.save(user);

        return new UserDto(user.getEmail(), user.getFullName(), user.getUsername(), user.getProfileImageUrl());
    }

    public void changePassword(String token, ChangePasswordDto dto) {
        String email = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

}
