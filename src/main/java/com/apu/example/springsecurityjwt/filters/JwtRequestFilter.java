package com.apu.example.springsecurityjwt.filters;

import com.apu.example.springsecurityjwt.services.MyUserDetailService;
import com.apu.example.springsecurityjwt.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
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


@Component
public class JwtRequestFilter  extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtHelper.resolveToken((HttpServletRequest) httpServletRequest);
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

    /*@Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String username =null;
        String jwt =null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = this.userDetailService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                  userDetails, userDetails.getPassword(), userDetails.getAuthorities()
                );

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }*/
}
