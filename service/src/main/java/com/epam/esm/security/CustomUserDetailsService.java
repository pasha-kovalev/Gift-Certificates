package com.epam.esm.security;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.UserServiceException;
import com.epam.esm.mapper.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.epam.esm.exception.ResourceBundleServiceErrorMessageKey.USER_NOT_PRESENT;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SecurityUser loadUserByUsername(String username) {
        UserDto user = userRepository.findByName(username)
                .map(userMapper::toDto)
                .orElseThrow(
                        () ->
                                new UserServiceException(
                                        USER_NOT_PRESENT, username));

        return new SecurityUser(user);
    }
}
