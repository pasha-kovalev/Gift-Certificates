package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.LoginDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static com.epam.esm.util.PaginationUtil.PAGE_NUMBER_DEFAULT_TEXT;
import static com.epam.esm.util.PaginationUtil.PAGE_SIZE_DEFAULT_TEXT;

/**
 * Controller for User entity
 */
@RequestMapping("/users")
@SecurityRequirement(name = "oauth2")
@Validated
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final UserAssembler userAssembler;

    @Autowired
    public UserController(UserService userService, UserAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    /**
     * Retrieves all Users for page
     *
     * @param size the page size
     * @param page the page number
     * @return the paged entity model of found Users
     */
    @GetMapping
    public PagedModel<EntityModel<UserDto>> all(
            @RequestParam(defaultValue = PAGE_SIZE_DEFAULT_TEXT) @Positive int size,
            @RequestParam(defaultValue = PAGE_NUMBER_DEFAULT_TEXT) @Positive int page,
            PagedResourcesAssembler<UserDto> pagedResourcesAssembler) {
        Page<UserDto> userDtoPage = userService.findAll(PageRequest.of(page - 1, size));
        return pagedResourcesAssembler.toModel(userDtoPage, userAssembler);
    }

    /**
     * Retrieves existing User by its id
     *
     * @param id the User id
     * @return found User
     */
    @GetMapping("{id}")
    public EntityModel<UserDto> one(@PathVariable @Positive Long id) {
        return userAssembler.toModel(userService.findById(id));
    }

    /**
     * Creates new user
     *
     * @param loginDto the login dto
     * @return the user dto of registered user
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public UserDto signup(@RequestBody @Valid LoginDto loginDto) {
        return userService.signup(loginDto.getUsername(), loginDto.getPassword());
    }
}
