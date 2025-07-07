package com.swapapp.swapappmockserver.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String fullName;
    private String username;
    private String profileImageUrl;
    private String location;
    private Boolean shipping;
    private String reputation;
    private List<UserAlbumDto> albums;
    private List<String> friends;
}