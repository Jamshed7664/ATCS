package com.nxtlife.efkon.enforcementconfigurator.ex.oauth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HttpMessageConverter<String> messageConverter;

    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.messageConverter = new StringHttpMessageConverter();
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(),e.getMessage(),e.getMessage());
        ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);
        outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);

        messageConverter.write(mapper.writeValueAsString(apiError), MediaType.APPLICATION_JSON, outputMessage);
    }
}