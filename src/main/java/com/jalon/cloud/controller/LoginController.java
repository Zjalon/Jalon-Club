package com.jalon.cloud.controller;

import com.jalon.cloud.dto.UserDTO;
import com.jalon.cloud.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "login")
    private ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return loginService.login(userDTO);
    }

}
