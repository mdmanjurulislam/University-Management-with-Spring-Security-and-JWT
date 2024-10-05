package com.security.SecurityExample.auth.dto;

import com.security.SecurityExample.auth.model.Role;
import com.security.SecurityExample.auth.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int userId;
    private Role role;
}
