package com.swapapp.swapappmockserver.service;

import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.dto.User.UserLoginDto;
import com.swapapp.swapappmockserver.dto.User.UserRegisterDto;
import com.swapapp.swapappmockserver.dto.User.LoginResponseDto;

public interface IUserService {
    UserDto register(UserRegisterDto userRegisterDto);
    LoginResponseDto login(UserLoginDto loginDto);
    UserDto getCurrentUser(String token);

}

