package com.tis.tis_web_app;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashingExample {
    public static void main(String[] args) {
        // Создаем экземпляр BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // Хэшируем пароль "1111"
        String hashedPassword = passwordEncoder.encode("1111");
        
        // Выводим хэшированный пароль
        System.out.println("Хэшированный пароль: " + hashedPassword);
    }
}