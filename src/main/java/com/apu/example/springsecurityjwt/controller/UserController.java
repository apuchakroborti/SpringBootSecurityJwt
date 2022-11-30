package com.apu.example.springsecurityjwt.controller;


import com.apu.example.springsecurityjwt.dto.*;
import com.apu.example.springsecurityjwt.entity.CustomUser;
import com.apu.example.springsecurityjwt.exceptions.GenericException;
import com.apu.example.springsecurityjwt.services.CustomUserService;
import com.apu.example.springsecurityjwt.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private CustomUserService customUserService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<APIResponse> createUser(@Valid @RequestBody UserCreateDto userCreateDto) throws GenericException {
        log.info("UserController::createUser request body {}", Utils.jsonAsString(userCreateDto));

        CustomUserDto customUserDto = customUserService.createUser(userCreateDto);
        log.debug("UserController::createUser response {}", Utils.jsonAsString(customUserDto));

        //Builder Design pattern
        APIResponse<CustomUserDto> responseDTO = APIResponse
                .<CustomUserDto>builder()
                .status("SUCCESS")
                .results(customUserDto)
                .build();

        log.info("UserController::createUser response {}", Utils.jsonAsString(responseDTO));

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse> searchUser(CustomUserSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException {
        log.info("UserController::searchUser start...");

        Page<CustomUser> customUserPage = customUserService.getUserList(criteria, pageable);

        List<CustomUserDto> customUserDtoList = Utils.toDtoList(customUserPage, CustomUserDto.class);

        APIResponse<List<CustomUserDto>> responseDTO = APIResponse
                .<List<CustomUserDto>>builder()
                .status("SUCCESS")
                .results(customUserDtoList)
                .pagination(new Pagination(customUserPage.getTotalElements(), customUserPage.getNumberOfElements(), customUserPage.getNumber(), customUserPage.getSize()))
                .build();

        log.info("UserController::searchUser end");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<APIResponse> getUserById(@PathVariable(name = "id") Long id ) throws GenericException {
        log.info("UserController::getUserById start...");

        CustomUserDto  customUserDto = customUserService.findUserById(id);

        APIResponse<CustomUserDto> responseDTO = APIResponse
                .<CustomUserDto>builder()
                .status("SUCCESS")
                .results(customUserDto)
                .build();

        log.info("UserController::getUserById end");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse>  updateUserById(@PathVariable(name = "id") Long id, @RequestBody CustomUserDto customUserDto) throws GenericException {

        log.info("UserController::updateEmployeeById start...");

        CustomUserDto  employeeDto = customUserService.updateUserById(id, customUserDto);

        APIResponse<CustomUserDto> responseDTO = APIResponse
                .<CustomUserDto>builder()
                .status("SUCCESS")
                .results(employeeDto)
                .build();

        log.info("UserController::updateEmployeeById end");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteUserById(@PathVariable(name = "id") Long id) throws GenericException {
        log.info("UserController::deleteUserById start...");

        Boolean res = customUserService.deleteUserById(id);

        APIResponse<Boolean> responseDTO = APIResponse
                .<Boolean>builder()
                .status("SUCCESS")
                .results(res)
                .build();

        log.info("UserController::deleteUserById end");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
