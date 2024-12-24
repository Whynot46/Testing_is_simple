package com.tis.tis_web_app;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordCheckExample {
    public static void main(String[] args) {
        // Создаем экземпляр BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "1111";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        String userInputPassword = "1111"; 
        boolean isPasswordMatch = passwordEncoder.matches(userInputPassword, hashedPassword);

        // Выводим результат проверки
        if (isPasswordMatch) {
            System.out.println("Пароль верный!");
        } else {
            System.out.println("Пароль неверный!");
        }
    }
}
