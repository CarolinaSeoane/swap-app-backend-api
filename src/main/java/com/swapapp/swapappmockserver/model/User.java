package com.swapapp.swapappmockserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
