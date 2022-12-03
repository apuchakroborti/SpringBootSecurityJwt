package com.apu.example.springsecurityjwt.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Error dto")
public class ErrorDto {
    private String field;
    private String message;
}
