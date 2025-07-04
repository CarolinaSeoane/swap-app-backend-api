package com.swapapp.swapappmockserver.model;

import com.swapapp.swapappmockserver.dto.User.UserAlbumDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String email;
    private String fullName;
    private String username;
    private String password;
    private String profileImageUrl;
    private String location;
    private Boolean shipping;
    private String reputation;
    private List<UserAlbumDto> albums;
    private List<String> friends;
}
