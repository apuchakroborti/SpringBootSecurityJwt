package com.apu.example.springsecurityjwt.filters;

import com.apu.example.springsecurityjwt.services.MyUserDetailService;
import com.apu.example.springsecurityjwt.util.JwtHelper;
import com.apu.example.springsecurityjwt.util.Utils;
import io.jsonwebtoken.Claims;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
public class JwtRequestFilter  extends OncePerRequestFilter {

    List<String> publicURL = Arrays.asList("/service-api/api/auth/authenticate");

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //TODO need to add global exception handler
        if (publicURL.contains(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }else {
            String accessToken = jwtHelper.resolveToken((HttpServletRequest) httpServletRequest);
            if(Utils.isNullOrEmpty(accessToken))throw new ServletException("Please provide authorization token!");
            String tokenType = jwtHelper.getTokenType(accessToken);

            if (accessToken != null && !tokenType.equalsIgnoreCase("access_token")) {
                throw new AuthenticationServiceException("Invalid Token");
            }
            Claims claims = jwtHelper.resolveClaims((HttpServletRequest) httpServletRequest);
            String username = jwtHelper.getUsername(claims);

            if (accessToken != null && claims != null
                    && jwtHelper.validateClaims(claims)) {
                UserDetails userDetails = this.userDetailService.loadUserByUsername(username);

                Authentication authentication = jwtHelper.getAuthentication(claims, (HttpServletRequest) httpServletRequest, accessToken, userDetails);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }
}
