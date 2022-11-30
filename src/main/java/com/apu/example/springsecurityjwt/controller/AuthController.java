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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    @Qualifier("userPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)throws  Exception{
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
        List<GrantedAuthority> authorities = userDetails.getAuthorities().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());

        List<String> authorityList = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        final String accessToken = jwtHelper.createToken(userDetails, "access_token", authorityList);
        final String refreshToken = jwtHelper.createToken(userDetails, "refresh_token", authorityList);

        return ResponseEntity.ok(new AuthenticationResponse(userDetails.getUsername(), accessToken, refreshToken, authorityList));
    }
}
