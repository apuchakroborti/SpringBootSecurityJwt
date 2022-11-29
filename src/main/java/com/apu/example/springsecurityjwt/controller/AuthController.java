package com.apu.example.springsecurityjwt.controller;

import com.apu.example.springsecurityjwt.models.AuthenticationRequest;
import com.apu.example.springsecurityjwt.models.AuthenticationResponse;
import com.apu.example.springsecurityjwt.services.MyUserDetailService;
import com.apu.example.springsecurityjwt.util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtHelper jwtTokenUtil;

    @Autowired
    @Qualifier("userPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/authenticate",method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)throws  Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }catch(BadCredentialsException e){
            throw new Exception("Incorrect username and password",e);
        }
        final UserDetails userDetails = userDetailService
                .loadUserByUsername(authenticationRequest.getUsername());

        if(!passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())){
            throw new Exception("Username or password is incorrect!");
        }

        final String jwt = jwtTokenUtil.createToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
