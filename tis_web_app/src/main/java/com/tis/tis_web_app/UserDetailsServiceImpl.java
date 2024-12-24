package com.tis.tis_web_app;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String fullname) throws UsernameNotFoundException {
        String[] name_parts = fullname.split(" ");
        
        if (name_parts.length != 3) {
            throw new IllegalArgumentException("Username must contain first name, patronymic, and last name separated by spaces");
        }
        
        User user = DataBase.get_user(name_parts[0], name_parts[1], name_parts[2]);
        
        if (user == null) {
            throw new UsernameNotFoundException("User  not found with username: " + fullname);
        }

        Collection<GrantedAuthority> authorities = getAuthorities(user.get_role_id());

        return new org.springframework.security.core.userdetails.User(
                user.get_fullname(),
                user.get_password_hash(),
                authorities // Передаем роли
        );
    }

    private Collection<GrantedAuthority> getAuthorities(int roleId) {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        Role role = DataBase.get_role(roleId); // Получаем роль из базы данных
        
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.get_name())); // Добавляем роль с префиксом "ROLE_"
        }
        
        return authorities;
    }

}