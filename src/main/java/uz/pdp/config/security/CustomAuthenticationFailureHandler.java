package uz.pdp.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;



@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        String errorMessage = URLEncoder.encode(exception.getMessage(), "UTF-8");
//        response.sendRedirect("/auth/login?error=" + errorMessage);
        response.sendRedirect("/auth/login?error=%s".formatted(exception.getMessage()));
    }
}