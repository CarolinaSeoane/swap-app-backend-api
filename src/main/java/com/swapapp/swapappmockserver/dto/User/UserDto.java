package com.swapapp.swapappmockserver.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String fullName;
    private String username;
    private String profileImageUrl;
}
