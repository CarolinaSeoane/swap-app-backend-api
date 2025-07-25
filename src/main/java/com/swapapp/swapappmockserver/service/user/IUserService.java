package com.swapapp.swapappmockserver.service.user;

import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.dto.User.UserLoginDto;
import com.swapapp.swapappmockserver.dto.User.UserRegisterDto;

import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import org.springframework.web.multipart.MultipartFile;

import com.swapapp.swapappmockserver.dto.User.ChangePasswordDto;
import com.swapapp.swapappmockserver.dto.User.LoginResponseDto;

import java.util.List;

public interface IUserService {
    LoginResponseDto register(UserRegisterDto userRegisterDto);
    LoginResponseDto login(UserLoginDto loginDto);
    UserDto getCurrentUser(String token);
    String saveProfileImage(String token, MultipartFile image);
    UserDto updateProfile(String token, UserDto dto);
    void changePassword(String token, ChangePasswordDto dto);
    UserDto getUserById(String id);
    UserDto getUserByEmail(String email);
    List<PossibleTrade> findPossibleTrades(UserDto user, UserDto friend);
    List<PossibleTrade> getPossibleTrades(String token);
    void addStickersToAlbum(UserDto user, List<Integer> sticker, Integer albumId);
    void removeStickersFromAlbum(UserDto user, List<Integer> sticker, Integer albumId);
}

