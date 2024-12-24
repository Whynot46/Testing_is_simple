package com.tis.tis_web_app;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {
    private int id;
    private String first_name;
    private String patronymic;
    private String last_name;
    private String password_hash;
    private int role_id;

    User(int id, String first_name, String patronymic, String last_name, String password_hash, int role_id) {
        this.id = id;
        this.first_name = first_name;
        this.patronymic = patronymic;
        this.last_name = last_name;
        this.password_hash = password_hash;
        this.role_id = role_id;
    }

    public Integer get_id() {
        return id;
    }

    public String get_first_name() {
        return first_name;
    }

    public String get_patronymic() {
        return patronymic;
    }

    public String get_last_name() {
        return last_name;
    }

    public String get_fullname() {
        return first_name + " " + patronymic + " " + last_name;
    }

    public int get_role_id() {
        return role_id;
    }

    public String get_role_name() {
        return DataBase.get_role_name(role_id);
    }

    public String get_password_hash() {
        return password_hash;
    }

    public String toString() {
        return "id: " + id + ", first_name: " + first_name + ", patronymic: " + patronymic + "birth_date: "
                + "password_hash: " + password_hash + "role_id: " + role_id;
    }

    // Реализация методов UserDetails
    @Override
    public String getUsername() {
        return get_fullname(); // Возвращаем полное имя как имя пользователя
    }

    @Override
    public String getPassword() {
        return password_hash; // Возвращаем хэш пароля
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        if (role_id == 3) { // Assuming 3 is the role ID for STUDENT
            authorities.add(new SimpleGrantedAuthority("STUDENT")); // No "ROLE_" prefix
        } else if (role_id == 2) { // Assuming 2 is the role ID for TEACHER
            authorities.add(new SimpleGrantedAuthority("TEACHER")); // No "ROLE_" prefix
        } else if (role_id == 1) { // Assuming 1 is the role ID for ADMIN
            authorities.add(new SimpleGrantedAuthority("ADMIN")); // No "ROLE_" prefix
        }
        return authorities;
    }

    public String getFullName() {
        return last_name + " " + first_name + " " + patronymic; // Возвращает полное имя
    }

}
