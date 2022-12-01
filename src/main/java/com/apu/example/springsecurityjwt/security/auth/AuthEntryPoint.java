package com.apu.example.springsecurityjwt.security.auth;


import com.apu.example.springsecurityjwt.dto.APIResponse;
import com.apu.example.springsecurityjwt.dto.ErrorDto;
import com.apu.example.springsecurityjwt.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

/**
 * @Author Apu
 */
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException, ServletException {
        logger.debug("authentication failed for target URL: {}", HttpUtils.determineTargetUrl(request));
        logger.debug("Inside AuthEntryPoint commence, creating custom error response");
        ErrorDto errorDetails = new ErrorDto("Authorization", ex.getMessage());
        APIResponse apiResponse = new APIResponse();
        apiResponse.setErrors(Collections.singletonList(errorDetails));
        apiResponse.setStatus(HttpStatus.UNAUTHORIZED.toString());

        response.setContentType("application/json");
        response.setStatus(403);
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, apiResponse);
        out.flush();
    }
}
