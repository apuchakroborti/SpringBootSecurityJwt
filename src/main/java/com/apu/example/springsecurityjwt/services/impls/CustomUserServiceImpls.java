package com.apu.example.springsecurityjwt.services.impls;

import com.apu.example.springsecurityjwt.Repository.AuthorityRepository;
import com.apu.example.springsecurityjwt.Repository.CustomUserRepository;
import com.apu.example.springsecurityjwt.Repository.UserCredentialRepository;
import com.apu.example.springsecurityjwt.dto.CustomUserDto;
import com.apu.example.springsecurityjwt.dto.CustomUserSearchCriteria;
import com.apu.example.springsecurityjwt.dto.UserCreateDto;
import com.apu.example.springsecurityjwt.entity.Authority;
import com.apu.example.springsecurityjwt.entity.CustomUser;
import com.apu.example.springsecurityjwt.entity.UserCredential;
import com.apu.example.springsecurityjwt.exceptions.GenericException;
import com.apu.example.springsecurityjwt.services.CustomUserService;
import com.apu.example.springsecurityjwt.specifications.CustomUserSearchSpecifications;
import com.apu.example.springsecurityjwt.util.Role;
import com.apu.example.springsecurityjwt.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@Slf4j
public class CustomUserServiceImpls implements CustomUserService {

    private final CustomUserRepository customUserRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    CustomUserServiceImpls(@Qualifier("userPasswordEncoder") PasswordEncoder passwordEncoder, CustomUserRepository customUserRepository,
                           UserCredentialRepository userCredentialRepository,
                           AuthorityRepository authorityRepository){
        this.passwordEncoder = passwordEncoder;
        this.customUserRepository = customUserRepository;
        this.userCredentialRepository = userCredentialRepository;
        this.authorityRepository = authorityRepository;
    }

    private UserCredential createUserCredential(UserCreateDto userCreateDto) throws GenericException{
        Optional<UserCredential> optionalUserCredential = userCredentialRepository.findByUsername(userCreateDto.getUsername());
        if(optionalUserCredential.isPresent()){
            throw new GenericException("User already exists");
        }
        UserCredential userCredential = new UserCredential();
        userCredential.setUsername(userCreateDto.getUsername());
        Authority authority = authorityRepository.findByName(Role.USER.getValue());
        userCredential.setAuthorities(Arrays.asList(authority));
        userCredential.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));

        return userCredentialRepository.save(userCredential);
    }

    @Override
    public CustomUserDto createUser(UserCreateDto userCreateDto) throws GenericException{
        Optional<CustomUser> optionalCustomUser = customUserRepository.findCustomUserByUserId(userCreateDto.getUserId());
        if (optionalCustomUser.isPresent()){
            log.error("CustomUserServiceImpls::createUser service:  userId: {} already exists", userCreateDto.getUserId());
            throw new GenericException("User already exists!");
        }

        CustomUser customUser = new CustomUser();


        UserCredential userCredential = this.createUserCredential(userCreateDto);
        customUser.setUserCredential(userCredential);

        Utils.copyProperty(userCreateDto, customUser);
        customUser = customUserRepository.save(customUser);

        CustomUserDto customUserDto = new CustomUserDto();

        Utils.copyProperty(customUser, customUserDto);

        return customUserDto;
    }


    @Override
    @CacheEvict(value = "user-cache", key = "'UserCache'+#id", beforeInvocation = true)
    @Cacheable(value = "user-cache", key = "'UserCache'+#id")
    public CustomUserDto findUserById(Long id) throws GenericException{
        log.info("CustomUserServiceImpls::findUserById start...");
        Optional<CustomUser> optionalCustomUser = customUserRepository.findById(id);
        if(optionalCustomUser.isPresent()){
            CustomUserDto customUserDto = new CustomUserDto();
            Utils.copyProperty(optionalCustomUser.get(), customUserDto);
            log.info("CustomUserServiceImpls::findUserById end, user found by id : {}", id);
            return customUserDto;
        }
        log.info("CustomUserServiceImpls::findUserById user not found bny id: {}", id);
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
            log.info("CustomUserServiceImpls::getUserList start...");

            Page<CustomUser> userPage = customUserRepository.findAll(
                    CustomUserSearchSpecifications.withId(criteria.getId())
                    .and(CustomUserSearchSpecifications.withFirstName(criteria.getFirstName()))
                    .and(CustomUserSearchSpecifications.withLastName(criteria.getLastName()))
                    .and(CustomUserSearchSpecifications.withEmail(criteria.getEmail()))
                    ,
                    pageable
            );
            log.info("CustomUserServiceImpls::getUserList end");

            return userPage;
        }catch (Exception e){
            log.error("CustomUserServiceImpls::getUserList exception occurred while fetching user");
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
