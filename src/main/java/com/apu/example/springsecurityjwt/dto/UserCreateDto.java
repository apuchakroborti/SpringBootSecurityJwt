package com.apu.example.springsecurityjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto implements Serializable {
    private Long id;

    @NotEmpty(message = "First Name should not be empty!")
    @NotNull(message = "First Name not be null!")
    private String firstName;

    @NotEmpty(message = "Last Name should not be empty!")
    @NotNull(message = "Last Name should not be null!")
    private String lastName;

    @NotEmpty(message = "Email should not be empty!")
    @NotNull(message = "Email should not be null!")
    private String email;

    @NotEmpty(message = "Username should not be empty!")
    @NotNull(message = "Username should not be null!")
    private String userName;

    @NotEmpty(message = "Password should not be empty!")
    @NotNull(message = "Password should not be null!")
    private String password;
}
