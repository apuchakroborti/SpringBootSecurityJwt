package com.apu.example.springsecurityjwt.models;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
public class AuthenticationResponse implements Serializable {
    private final String username;
    private final String accessToken;
    private final String refreshToken;
    private final List<String> authorities;

    public AuthenticationResponse(String username, String accessToken, String refreshToken, List<String> authorities) {
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.authorities = authorities;
    }
}
