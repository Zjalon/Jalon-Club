package com.jalon.cloud.config.security;

import com.jalon.cloud.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomerUserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        // 根据用户名查找用户
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword("$2a$10$qyuSBd1zUk/V20f1WyH5BOL8xH6x7zQZN3IzwgtS3A6.ettf.fCTy");
        if (userDTO == null) {
            throw new UsernameNotFoundException("Username :" + username + "not found");
        }
        return new SecurityUser(userDTO);
    }
}
