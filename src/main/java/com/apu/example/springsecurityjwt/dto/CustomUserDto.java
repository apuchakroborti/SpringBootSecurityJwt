package com.apu.example.springsecurityjwt.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Custom User Dto")
public class CustomUserDto implements Serializable {
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
