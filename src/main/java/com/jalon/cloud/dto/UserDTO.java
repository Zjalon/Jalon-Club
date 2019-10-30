package com.jalon.cloud.dto;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class UserDTO extends BaseDTO {
    private String username;
    private String password;
    private String token;
    private Set<RoleDTO> roles = new LinkedHashSet<>();
}
