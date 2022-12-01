package com.apu.example.springsecurityjwt.security;

import com.apu.example.springsecurityjwt.filters.JwtRequestFilter;
import com.apu.example.springsecurityjwt.services.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    /*public static final String[] publicURL = {
            "/service-api/api/user/**",
            "/service-api/api/auth/authenticate",
            "/service-api/swagger-ui.html",
            "/service-api/v3/api-docs/**",
            "/service-api/v3/api-docs",
            "/service-api/swagger-ui/**",
            "/service-api/v2/api-docs/**",
            "/service-api/swagger-resources/**",
            "/api/user/**",
            "/api/auth/authenticate",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**"};*/

    public static final String[] publicURL = {
            "/api/user/**",
            "/api/auth/authenticate",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**"};

    @Autowired
    private MyUserDetailService myUserDetailService;


    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(myUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       //for jwt token generation, validation
        http
        .csrf()
        .disable()
        .authorizeRequests()
//        .antMatchers("/api/auth/authenticate").permitAll()
        .antMatchers(publicURL).permitAll()
        .anyRequest().authenticated()
        .and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean()throws Exception{
        return super.authenticationManagerBean();
    }
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
