package com.apu.example.springsecurityjwt.filters;

import com.apu.example.springsecurityjwt.services.MyUserDetailService;
import com.apu.example.springsecurityjwt.util.JwtHelper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Component
@Order(1)
@Slf4j
public class JwtRequestFilter  extends OncePerRequestFilter {

    List<String> publicURL = Arrays.asList(
            "/api/auth/authenticate",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/swagger-ui/**",
            "/swagger-ui/",
            "/v2/api-docs/**",
            "/swagger-resources/**");

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //TODO need to add global exception handler
        log.info("JwtRequestFilter::doFilterInternal start...");
        log.info("JwtRequestFilter::doFilterInternal Url: "+httpServletRequest.getRequestURI().substring(12));
        if (publicURL.contains(httpServletRequest.getRequestURI().substring(12))) {
            log.info("JwtRequestFilter::doFilterInternal this is from public url");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }else {
            String accessToken = jwtHelper.resolveToken((HttpServletRequest) httpServletRequest);
            if(accessToken!=null){
                String tokenType = jwtHelper.getTokenType(accessToken);

                if (accessToken != null && !tokenType.equalsIgnoreCase("access_token")) {
                    log.info("JwtRequestFilter::doFilterInternal Invalid token");
                    throw new AuthenticationServiceException("Invalid Token");
                }
                Claims claims = jwtHelper.resolveClaims((HttpServletRequest) httpServletRequest);
                String username = jwtHelper.getUsername(claims);

                if (accessToken != null && claims != null
                        && jwtHelper.validateClaims(claims)) {
                    UserDetails userDetails = this.userDetailService.loadUserByUsername(username);

                    Authentication authentication = jwtHelper.getAuthentication(claims, (HttpServletRequest) httpServletRequest, accessToken, userDetails);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("JwtRequestFilter::doFilterInternal authentication done for the username: {} and authentication result: {}", username, authentication.isAuthenticated());
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        log.info("JwtRequestFilter::doFilterInternal end ");

    }
}
