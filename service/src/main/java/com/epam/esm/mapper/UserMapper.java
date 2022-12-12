package com.epam.esm.mapper;

import com.epam.esm.domain.User;
import com.epam.esm.dto.UserDto;
import org.mapstruct.Mapper;

/**
 * Mapper for User
 */
@Mapper
public interface UserMapper {
    UserDto toDto(User entity);

    User toEntity(UserDto dto);
}
