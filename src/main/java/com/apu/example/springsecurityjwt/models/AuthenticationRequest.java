package com.apu.example.springsecurityjwt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @NotNull(message = "username must not be null!")
    @NotNull(message = "username must not be empty!")
    private String username;
    @NotNull(message = "password must not be null!")
    @NotNull(message = "password must not be empty!")
    private String password;
}
