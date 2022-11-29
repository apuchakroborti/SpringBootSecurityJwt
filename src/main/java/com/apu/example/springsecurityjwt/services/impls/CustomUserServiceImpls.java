package com.apu.example.springsecurityjwt.services.impls;

import com.apu.example.springsecurityjwt.Repository.CustomUserRepository;
import com.apu.example.springsecurityjwt.Repository.UserCredentialRepository;
import com.apu.example.springsecurityjwt.dto.CustomUserDto;
import com.apu.example.springsecurityjwt.dto.CustomUserSearchCriteria;
import com.apu.example.springsecurityjwt.dto.UserCreateDto;
import com.apu.example.springsecurityjwt.entity.CustomUser;
import com.apu.example.springsecurityjwt.entity.UserCredential;
import com.apu.example.springsecurityjwt.exceptions.GenericException;
import com.apu.example.springsecurityjwt.services.CustomUserService;
import com.apu.example.springsecurityjwt.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserServiceImpls implements CustomUserService {

    private final CustomUserRepository customUserRepository;
    private final UserCredentialRepository userCredentialRepository;


    @Autowired
    CustomUserServiceImpls(CustomUserRepository customUserRepository,
                           UserCredentialRepository userCredentialRepository){
        this.customUserRepository = customUserRepository;
        this.userCredentialRepository = userCredentialRepository;
    }

    private UserCredential createUserCredential(UserCreateDto userCreateDto) throws GenericException{
        Optional<UserCredential> optionalUserCredential = userCredentialRepository.findByUsername(userCreateDto.getUserName());
        if(optionalUserCredential.isPresent()){
            throw new GenericException("User already exists");
        }
        UserCredential userCredential = new UserCredential();
        userCredential.setUsername(userCreateDto.getUserName());
        userCredential.setPassword(userCreateDto.getPassword());

        return userCredentialRepository.save(userCredential);
    }

    @Override
    public CustomUserDto createUser(UserCreateDto userCreateDto) throws GenericException{
        CustomUser customUser = new CustomUser();

        Utils.copyProperty(userCreateDto, customUser);
        UserCredential userCredential = this.createUserCredential(userCreateDto);
        customUser.setUserCredential(userCredential);
        customUser = customUserRepository.save(customUser);

        CustomUserDto customUserDto = new CustomUserDto();

        Utils.copyProperty(customUser, customUserDto);

        return customUserDto;
    }


    @Override
    public CustomUserDto findUserById(Long id) throws GenericException{
        Optional<CustomUser> optionalCustomUser = customUserRepository.findById(id);
        if(optionalCustomUser.isPresent()){
            CustomUserDto customUserDto = new CustomUserDto();
            Utils.copyProperty(optionalCustomUser.get(), customUserDto);
            return customUserDto;
        }
        throw new GenericException("User not found!");
    }

    @Override
    public CustomUserDto updateUserById(Long id, CustomUserDto customUserDto) throws GenericException{
        Optional<CustomUser> optionalCustomUser = customUserRepository.findById(id);
        if(optionalCustomUser.isPresent()){
            CustomUser customUser = optionalCustomUser.get();
            if(!Utils.isNullOrEmpty(customUserDto.getFirstName())){
                customUser.setFirstName(customUserDto.getFirstName());
            }
            if(!Utils.isNullOrEmpty(customUserDto.getLastName())){
                customUser.setLastName(customUserDto.getLastName());
            }
            customUserRepository.save(customUser);
            Utils.copyProperty(customUser, customUserDto);
            return customUserDto;
        }
        throw new GenericException("User not found!");

    }

    @Override
    public Page<CustomUser> getUserList(CustomUserSearchCriteria criteria, Pageable pageable) throws GenericException{
        try {

            Page<CustomUser> userPage = customUserRepository.getAllUsers(pageable);

            return userPage;
        }catch (Exception e){
            throw new GenericException("exception occurred while fetching user list!");
        }
    }

    @Override
    public Boolean  deleteUserById(Long id) throws GenericException{

        Optional<CustomUser> optionalCustomUser = customUserRepository.findById(id);
        if(optionalCustomUser.isPresent()){
            customUserRepository.delete(optionalCustomUser.get());
            return true;
        }
        throw new GenericException("User not found!");

    }
}
