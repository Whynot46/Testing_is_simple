package com.tis.tis_web_app;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordCheckExample {
    public static void main(String[] args) {
        // Создаем экземпляр BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Хэшируем пароль "password123" (это делается при регистрации)
        String rawPassword = "1111";
        String hashedPassword = passwordEncoder.encode(rawPassword);
        System.out.println(hashedPassword);
        
        // Сохраняем hashedPassword в базе данных

        // Проверка пароля при входе
        String userInputPassword = "1111"; // Пароль, введенный пользователем
        boolean isPasswordMatch = passwordEncoder.matches(userInputPassword, hashedPassword);

        // Выводим результат проверки
        if (isPasswordMatch) {
            System.out.println("Пароль верный!");
        } else {
            System.out.println("Пароль неверный!");
        }
    }
}
