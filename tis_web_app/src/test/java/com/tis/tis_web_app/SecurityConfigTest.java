package com.tis.tis_web_app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigTest {

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(); // Создание бина для кастомного обработчика
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler(); // Создание бина для обработчика неудачной аутентификации
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize // Использование authorizeHttpRequests
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/teacher/**").hasAuthority("TEACHER")
                .requestMatchers("/student/**").hasAuthority("STUDENT")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form // Использование лямбда-выражений для конфигурации
                .loginPage("/login") // Укажите страницу входа
                .successHandler(customAuthenticationSuccessHandler()) // Использование кастомного обработчика
                .failureHandler(customAuthenticationFailureHandler()) // Использование обработчика неудачной аутентификации
                .permitAll()
            )
            .logout(logout -> logout // Конфигурация выхода
                .permitAll()
            );

        return http.build(); // Возвращаем построенный объект HttpSecurity
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Возвращаем AuthenticationManager
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Используем BCrypt для кодирования паролей
    }
}