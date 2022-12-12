package com.epam.esm.dto;

import com.epam.esm.domain.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * DTO that transfers {@link com.epam.esm.domain.User} data
 */
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto extends RepresentationModel<UserDto> implements BaseDto {
    @Positive
    private Long id;

    @Size(min = 3, max = 45)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String name;

    @JsonIgnore
    private String password;

    private Role role;
}
