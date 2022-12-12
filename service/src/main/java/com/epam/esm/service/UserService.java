package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface represents service for Tag entity
 */
public interface UserService {
    Page<UserDto> findAll(Pageable pageable);

    /**
     * Finds a User by its id
     *
     * @param id User id
     * @return found User
     */
    UserDto findById(long id);

    UserDto findByName(String name);

    UserDto signup(String username, String password);
}
