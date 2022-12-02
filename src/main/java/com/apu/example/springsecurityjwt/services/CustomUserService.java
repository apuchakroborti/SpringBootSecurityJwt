package com.apu.example.springsecurityjwt.services;

import com.apu.example.springsecurityjwt.dto.CustomUserDto;
import com.apu.example.springsecurityjwt.dto.CustomUserSearchCriteria;
import com.apu.example.springsecurityjwt.dto.UserCreateDto;
import com.apu.example.springsecurityjwt.entity.CustomUser;
import com.apu.example.springsecurityjwt.exceptions.GenericException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserService {
    CustomUserDto createUser(UserCreateDto userCreateDto) throws GenericException;
    CustomUserDto findUserById(Long id, boolean isCacheable) throws GenericException;
    CustomUserDto updateUserById(Long id, CustomUserDto employeeBean) throws GenericException;
    Page<CustomUser> getUserList(CustomUserSearchCriteria criteria, Pageable pageable) throws GenericException;
    Boolean  deleteUserById(Long id) throws GenericException;
}
