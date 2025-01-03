package com.tis.tis_web_app;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Определяем целевую страницу в зависимости от роли
        String redirectUrl = determineTargetUrl(authentication);
        response.sendRedirect(redirectUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("");
    
        switch (role) {
            case "ROLE_ADMIN":
                return "/admin/profile"; // URL for admin
            case "ROLE_TEACHER":
                return "/teacher/profile"; // URL for teacher
            case "ROLE_STUDENT":
                return "/student/profile"; // URL for student
            default:
                return "/"; // Default URL
        }
    }

}