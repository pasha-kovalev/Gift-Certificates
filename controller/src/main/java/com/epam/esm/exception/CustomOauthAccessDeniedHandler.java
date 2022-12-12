package com.epam.esm.exception;

import com.epam.esm.locale.MessageTranslator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.esm.exception.ResourceBundleMessageKey.OAUTH2_ACCESS_DENIES;

@Component
public class CustomOauthAccessDeniedHandler implements AccessDeniedHandler {
    private final CustomOauthResponseExceptionTranslator exceptionTranslator;
    private final MessageTranslator messageTranslator;

    @Autowired
    CustomOauthAccessDeniedHandler(CustomOauthResponseExceptionTranslator exceptionTranslator, MessageTranslator messageTranslator) {
        this.exceptionTranslator = exceptionTranslator;
        this.messageTranslator = messageTranslator;
    }

    @SneakyThrows
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseEntity<?> responseEntity;
        response.setContentType("application/json;charset=UTF-8");
        if (accessDeniedException.getCause() instanceof OAuth2Exception e) {
            responseEntity = exceptionTranslator.translate(e);
        } else {
            String message = messageTranslator.getMessageForLocale(OAUTH2_ACCESS_DENIES, null);
            ErrorResponse errorResponse = new ErrorResponse(message, ExceptionCode.OAUTH2_FORBIDDEN_CODE);
            responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }
        String json = new ObjectMapper().writeValueAsString(responseEntity.getBody());
        response.setStatus(responseEntity.getStatusCodeValue());
        response.getWriter().write(json);
    }
}
