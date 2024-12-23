package com.tis.tis_web_app;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class TeacherController {

        @GetMapping("/teacher/profile")
        public String show_teacher_profile(Model model) {
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
            
            return "teacher_profile"; // Return the view name
        }

}
