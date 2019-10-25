package com.jalon.cloud.dto;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class MenuDTO {
    private String menuName;
}
