package com.epam.esm.validator.impl;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotPresentException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private final User user = new User(1L, "user1", "pass", Role.USER);
    private final UserDto userDto = new UserDto(1L, "user1", "pass", Role.USER);

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @AfterEach
    public void verifyMockEach() {
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testFindAllUsersShouldReturnListOfAllUsers() {
        List<User> users = List.of(user);
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> page = new PageImpl<>(users, pageable, 5);
        Page<UserDto> expectedPage = new PageImpl<>(List.of(userDto), page.getPageable(), 5);

        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userMapper.toDto(any())).thenReturn(userDto);
        Page<UserDto> actualPage = userService.findAll(pageable);

        assertEquals(expectedPage, actualPage);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void testFindByIdShouldReturnFoundUserWithGivenId() {
        final long ID_ONE = 1;
        when(userRepository.findById(ID_ONE)).thenReturn(Optional.of(user));
        when(userMapper.toDto(any())).thenReturn(userDto);

        UserDto actual = userService.findById(ID_ONE);

        assertEquals(userDto, actual);
    }

    @Test
    void testFindByIdShouldThrowExceptionIfUserNotFound() {
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(EntityNotPresentException.class, () -> userService.findById(0));
    }
}
