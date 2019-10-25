package com.jalon.cloud.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RoleDTO {
    private String roleName;
    private Set<MenuDTO> menus;
}
