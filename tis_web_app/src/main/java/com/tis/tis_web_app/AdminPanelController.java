package com.tis.tis_web_app;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelController {

    @GetMapping("/admin_panel")
    public String show_admin_panel(Model model) {
        return "admin_panel";
    }

    @GetMapping("/admin_panel/users")
    public String show_users(Model model) {
        List<User> users = DataBase.get_users();
        model.addAttribute("users", users);
        return "users_list";
    }

    @GetMapping("/admin_panel/roles")
    public String show_roles(Model model) {
        List<Role> roles = DataBase.get_roles();
        model.addAttribute("roles", roles);
        return "roles_list";
    }

    @GetMapping("/admin_panel/tests")
    public String show_tests(Model model) {
        List<Test> tests = DataBase.get_tests();
        model.addAttribute("tests", tests); // Добавляем список ролей в модель
        return "tests_list"; // Возвращаем имя шаблона
    }

    @GetMapping("/admin_panel/test_results")
    public String show_test_results(Model model) {
        List<TestResult> test_results = DataBase.get_test_results();
        model.addAttribute("test_results", test_results); // Добавляем список ролей в модель
        return "test_results_list"; // Возвращаем имя шаблона
    }

    @GetMapping("/admin_panel/tasks")
    public String show_tasks(Model model) {
        List<Task> tasks = DataBase.get_tasks();
        model.addAttribute("tasks", tasks); // Добавляем список ролей в модель
        return "tasks_list"; // Возвращаем имя шаблона
    }

    @GetMapping("/admin_panel/topics")
    public String show_topics(Model model) {
        List<Topic> topics = DataBase.get_topics();
        model.addAttribute("topics", topics); // Добавляем список ролей в модель
        return "topics_list"; // Возвращаем имя шаблона
    }

}