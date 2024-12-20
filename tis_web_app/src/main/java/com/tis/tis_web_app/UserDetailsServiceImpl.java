package com.tis.tis_web_app;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] name_parts = username.split(" ");
        
        if (name_parts.length < 3) {
            throw new IllegalArgumentException("Username must contain first name, patronymic, and last name separated by spaces.");
        }

        User user = DataBase.get_user(name_parts[0], name_parts[1], name_parts[2]);

        if (user == null) {
            throw new UsernameNotFoundException("User  not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.get_first_name(),
                user.get_password(),
                new ArrayList<>()
        );
    }
}