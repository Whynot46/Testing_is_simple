package com.tis.tis_web_app;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class AdminController {

    @GetMapping("/admin/profile")
    public String show_admin_profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            String[] username_parts = username.split(" ");
            User user = DataBase.get_user(username_parts[2], username_parts[0], username_parts[1]);
            
            model.addAttribute("user", user); // Add the user object to the model
        } else {
            return "redirect:/login"; // Перенаправление на страницу входа
        }
        
        return "admin_profile"; // Return the view name
    }

    @GetMapping("/admin/tests_list")
    public String show_tests_list(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            String[] username_parts = username.split(" ");
            User user = DataBase.get_user(username_parts[2], username_parts[0], username_parts[1]);
            
            model.addAttribute("user", user); // Add the user object to the model
        } else {
            return "redirect:/login"; // Перенаправление на страницу входа
        }
        
        return "admin_tests_list"; // Return the view name
    }
    
    @GetMapping("/admin/edit_test")
    public String show_edit_test(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            String[] username_parts = username.split(" ");
            User user = DataBase.get_user(username_parts[2], username_parts[0], username_parts[1]);
            
            model.addAttribute("user", user); // Add the user object to the model
        } else {
            return "redirect:/login"; // Перенаправление на страницу входа
        }
        
        return "edit_test"; // Return the view name
    }

    @GetMapping("/admin/students_results")
    public String show_students_results(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            String[] username_parts = username.split(" ");
            User user = DataBase.get_user(username_parts[2], username_parts[0], username_parts[1]);
            
            model.addAttribute("user", user); // Add the user object to the model
        } else {
            return "redirect:/login"; // Перенаправление на страницу входа
        }
        
        return "students_results"; // Return the view name
    }
    
    @GetMapping("/current-user")
    public String getCurrentUser (Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // Получаем имя пользователя
            String username = authentication.getName(); // Это имя пользователя
            model.addAttribute("username", username);
            
            // Получаем principal и добавляем его в модель
            Object principal = authentication.getPrincipal();
            model.addAttribute("principal", principal);
            
            // Получаем роли пользователя
            List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
            
            // Проверяем, есть ли роли
            if (!roles.isEmpty()) {
                model.addAttribute("role", roles.get(0)); // Добавляем первую роль в модель
            } else {
                model.addAttribute("role", "Нет ролей"); // Если ролей нет
            }
        } else {
            model.addAttribute("username", null);
            model.addAttribute("principal", null);
            model.addAttribute("role", null);
        }
        return "current_user"; // Возвращаем имя шаблона
    }

    @GetMapping("/admin/panel")
    public String show_admin_panel(Model model) {
        return "admin_panel";
    }

    @GetMapping("/admin/panel/users")
    public String show_users(Model model) {
        List<User> users = DataBase.get_users();
        model.addAttribute("users", users);
        return "users_list";
    }

    @GetMapping("/admin/panel/roles")
    public String show_roles(Model model) {
        List<Role> roles = DataBase.get_roles();
        model.addAttribute("roles", roles);
        return "roles_list";
    }

    @GetMapping("/admin/panel/tests")
    public String show_tests(Model model) {
        List<Test> tests = DataBase.get_tests();
        model.addAttribute("tests", tests); // Добавляем список ролей в модель
        return "tests_list"; // Возвращаем имя шаблона
    }

    @GetMapping("/admin/panel/test_results")
    public String show_test_results(Model model) {
        List<TestResult> test_results = DataBase.get_test_results();
        model.addAttribute("test_results", test_results); // Добавляем список ролей в модель
        return "test_results_list"; // Возвращаем имя шаблона
    }

    @GetMapping("/admin/panel/tasks")
    public String show_tasks(Model model) {
        List<Task> tasks = DataBase.get_tasks();
        model.addAttribute("tasks", tasks); // Добавляем список ролей в модель
        return "tasks_list"; // Возвращаем имя шаблона
    }

    @GetMapping("/admin/panel/topics")
    public String show_topics(Model model) {
        List<Topic> topics = DataBase.get_topics();
        model.addAttribute("topics", topics); // Добавляем список ролей в модель
        return "topics_list"; // Возвращаем имя шаблона
    }

}