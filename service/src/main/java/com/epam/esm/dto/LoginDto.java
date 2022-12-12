package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginDto {

    @Size(min = 3, max = 45)
    @Pattern(regexp = "^\\w[\\w\\s]+")
    private String username;

    @Size(min = 3, max = 45)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String password;
}
