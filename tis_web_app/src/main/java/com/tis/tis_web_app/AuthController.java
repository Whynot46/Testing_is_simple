package com.tis.tis_web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true); // Добавляем атрибут для отображения ошибки
        }
        return "login";
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