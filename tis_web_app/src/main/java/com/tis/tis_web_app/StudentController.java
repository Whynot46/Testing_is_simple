package com.tis.tis_web_app;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @GetMapping("/student/profile")
    public String show_student_profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.info("Userdetails: {}", userDetails);
            String username = userDetails.getUsername();
            String[] username_parts = username.split(" ");
            logger.info("Userdetails username: {}", userDetails.getUsername());
            User user = DataBase.get_user(username_parts[2], username_parts[0], username_parts[1]);
            logger.info("User: {}", user.toString());
            
            model.addAttribute("user", user); // Add the user object to the model
        } else {
            logger.warn("Authentication is null or user is not authenticated.");
            return "redirect:/login"; // Перенаправление на страницу входа
        }
        
        return "student_profile"; // Return the view name
    }

    @GetMapping("/student/tests_list")
    public String show_student_tests_list(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.info("Userdetails: {}", userDetails);
            String username = userDetails.getUsername();
            String[] username_parts = username.split(" ");
            logger.info("Userdetails username: {}", userDetails.getUsername());
            User user = DataBase.get_user(username_parts[2], username_parts[0], username_parts[1]);
            logger.info("User: {}", user.toString());
            
            model.addAttribute("user", user); // Add the user object to the model
        } else {
            logger.warn("Authentication is null or user is not authenticated.");
            return "redirect:/login"; // Перенаправление на страницу входа
        }
        
        return "student_tests_list"; // Return the view name
    }

    @GetMapping("/student/test")
    public String show_student_test(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.info("Userdetails: {}", userDetails);
            String username = userDetails.getUsername();
            String[] username_parts = username.split(" ");
            logger.info("Userdetails username: {}", userDetails.getUsername());
            User user = DataBase.get_user(username_parts[2], username_parts[0], username_parts[1]);
            logger.info("User: {}", user.toString());
            
            model.addAttribute("user", user); // Add the user object to the model
        } else {
            logger.warn("Authentication is null or user is not authenticated.");
            return "redirect:/login"; // Перенаправление на страницу входа
        }
        
        return "test"; // Return the view name
    }

    @GetMapping("/student/student_results")
    public String show_student_student_results(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.info("Userdetails: {}", userDetails);
            String username = userDetails.getUsername();
            String[] username_parts = username.split(" ");
            logger.info("Userdetails username: {}", userDetails.getUsername());
            User user = DataBase.get_user(username_parts[2], username_parts[0], username_parts[1]);
            logger.info("User: {}", user.toString());
            
            model.addAttribute("user", user); // Add the user object to the model
        } else {
            logger.warn("Authentication is null or user is not authenticated.");
            return "redirect:/login"; // Перенаправление на страницу входа
        }
        
        return "students_results"; // Return the view name
    }
}
