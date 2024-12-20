package com.tis.tis_web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Возвращаем имя шаблона для страницы входа
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // Возвращаем имя шаблона для страницы входа
    }

}
