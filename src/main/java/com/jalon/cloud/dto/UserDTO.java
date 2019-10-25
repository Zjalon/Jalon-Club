package com.jalon.cloud.dto;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class UserDTO {
    private String username;
    private String password;
    private Set<RoleDTO> roles = new LinkedHashSet<>();
}
