package com.tis.tis_web_app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String show_login_page() {
        return "login"; // возвращает страницу входа
    }

    @GetMapping("/logout")
    public String logout() {
        // Здесь можно добавить логику, если нужно, например, логирование
        return "redirect:/login"; // Перенаправление на страницу авторизации
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // Возвращаем имя шаблона для страницы регистрации
    }
}