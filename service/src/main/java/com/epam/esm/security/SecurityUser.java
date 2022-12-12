package com.epam.esm.security;

import com.epam.esm.dto.UserDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.List;

/**
 * Provides {@link com.epam.esm.dto.UserDto} data with granted authorities
 */
public class SecurityUser extends User {
    public static final String ROLE_PREFIX = "ROLE_";
    @Serial
    private static final long serialVersionUID = -3853913335046759609L;

    public SecurityUser(UserDto user) {
        super(user.getName(), user.getPassword(), List.of(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole())));
    }
}
