package com.swapapp.swapappmockserver.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private String email;
    private String fullName;
    private String username;
    private List<UserAlbumDto> albums;
    private List<String> friends;
}
