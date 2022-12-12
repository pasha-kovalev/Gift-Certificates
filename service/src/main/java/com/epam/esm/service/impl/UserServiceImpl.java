package com.epam.esm.service.impl;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotPresentException;
import com.epam.esm.exception.ResourceBundleServiceErrorMessageKey;
import com.epam.esm.exception.UserServiceException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.epam.esm.exception.ResourceBundleServiceErrorMessageKey.USER_EXISTS;

@Log4j2
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    public UserDto findById(long id) {
        return userRepository
                .findById(id)
                .map(userMapper::toDto)
                .orElseThrow(
                        () ->
                                new EntityNotPresentException(
                                        ResourceBundleServiceErrorMessageKey.USER_NOT_PRESENT, id));
    }

    @Override
    public UserDto findByName(String name) {
        return userRepository.findByName(name)
                .map(userMapper::toDto)
                .orElseThrow(
                        () ->
                                new UserServiceException(
                                        ResourceBundleServiceErrorMessageKey.USER_NOT_PRESENT, name));
    }

    @Override
    public UserDto signup(String username, String password) {
        if (userRepository.findByName(username).isPresent()) {
            throw new UserServiceException(USER_EXISTS, username);
        }
        return userMapper.toDto(userRepository.save(
                new User(null, username, passwordEncoder.encode(password), Role.USER)
        ));
    }
}
