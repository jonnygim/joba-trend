package com.plus1250.jobaTrend.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        exception.printStackTrace();
        writePrintErrorResponse(response, exception);

    }
    private void writePrintErrorResponse(HttpServletResponse response, AuthenticationException exception) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = new HashMap<>();
            String message = getExceptionMessage(exception);

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            PrintWriter writer = response.getWriter();

            responseMap.put("status", 401);
            responseMap.put("message", message);

            writer.write(objectMapper.writeValueAsString(responseMap));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getExceptionMessage(AuthenticationException exception) {

        if(exception instanceof AuthenticationServiceException) {
//            request.setAttribute("LoginFailMessage", "죄송합니다. 시스템에 오류가 발생했습니다.");
            return "죄송합니다. 시스템에 오류가 발생했습니다.";
        }

        else if(exception instanceof BadCredentialsException) {
            return "아이디 또는 비밀번호가 일치하지 않습니다.";
        }
        else if(exception instanceof DisabledException) {
            return "현재 사용할 수 없는 계정입니다.";
        }
        else if(exception instanceof LockedException) {
            return "현재 잠긴 계정입니다.";
        }
        else if(exception instanceof AccountExpiredException) {
            return "이미 만료된 계정입니다.";
        }
        else if(exception instanceof CredentialsExpiredException) {
            return "비밀번호가 만료된 계정입니다.";
        }
        else {
            return "계정을 찾을 수 없습니다.";
        }
    }
}