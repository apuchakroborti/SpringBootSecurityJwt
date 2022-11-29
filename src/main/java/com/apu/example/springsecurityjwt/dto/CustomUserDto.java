package com.apu.example.springsecurityjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
