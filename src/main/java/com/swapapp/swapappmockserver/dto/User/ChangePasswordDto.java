package com.swapapp.swapappmockserver.dto.User;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;
}
