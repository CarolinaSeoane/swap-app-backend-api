package com.swapapp.swapappmockserver.service;

import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.dto.User.UserLoginDto;
import com.swapapp.swapappmockserver.dto.User.UserRegisterDto;

import org.springframework.web.multipart.MultipartFile;

import com.swapapp.swapappmockserver.dto.User.ChangePasswordDto;
import com.swapapp.swapappmockserver.dto.User.LoginResponseDto;

public interface IUserService {
    LoginResponseDto register(UserRegisterDto userRegisterDto);
    LoginResponseDto login(UserLoginDto loginDto);
    UserDto getCurrentUser(String token);
    String saveProfileImage(String token, MultipartFile image);
    UserDto updateProfile(String token, UserDto dto);
    void changePassword(String token, ChangePasswordDto dto);
}

