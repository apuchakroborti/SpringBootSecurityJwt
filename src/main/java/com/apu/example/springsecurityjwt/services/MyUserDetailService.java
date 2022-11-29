package com.apu.example.springsecurityjwt.services;

import com.apu.example.springsecurityjwt.Repository.UserCredentialRepository;
import com.apu.example.springsecurityjwt.entity.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        return new User("apucsedu","@#$%apu123", new ArrayList<>());
        Optional<UserCredential> optionalUserCredential = userCredentialRepository.findByUsername(userName);
        if(optionalUserCredential.isPresent()){
            return new User(optionalUserCredential.get().getUsername(), optionalUserCredential.get().getPassword(), optionalUserCredential.get().getAuthorities());
        }
        throw new UsernameNotFoundException("User not found");
    }


}
