package com.jalon.cloud.service;

import com.jalon.cloud.config.security.CustomerUserService;
import com.jalon.cloud.dto.UserDTO;
import com.jalon.cloud.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class LoginService {
    @Autowired
    private CustomerUserService customerUserService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisUtil redisUtil;

    public ResponseEntity<UserDTO> login(UserDTO userDTO) {
        UserDTO dto = new UserDTO();
        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Reload password post-security so we can generate token
            final UserDetails userDetails = customerUserService.loadUserByUsername(userDTO.getUsername());
            // 持久化的redis
            String token = new BCryptPasswordEncoder().encode(userDetails.getUsername());
            redisUtil.set(token,userDetails.getUsername());
            dto.setToken(token);
            dto.setUsername(userDetails.getUsername());
            dto.setBackCode("200");
            dto.setBackMsg("登录成功");
        } catch (Exception e) {
            dto.setBackCode("401");
            dto.setBackMsg(String.valueOf(e.getMessage()));
        }

        return ResponseEntity.ok(dto);
    }
}
