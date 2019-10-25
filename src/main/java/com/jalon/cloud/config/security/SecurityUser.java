package com.jalon.cloud.config.security;

import com.jalon.cloud.dto.RoleDTO;
import com.jalon.cloud.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class SecurityUser extends UserDTO implements UserDetails {

    public SecurityUser(UserDTO userDTO) {
        this.setUsername(userDTO.getUsername());
        this.setPassword(userDTO.getPassword());
        this.setRoles(userDTO.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Set<RoleDTO> roles = super.getRoles();
        if (roles != null) {
            for (RoleDTO role : roles) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
