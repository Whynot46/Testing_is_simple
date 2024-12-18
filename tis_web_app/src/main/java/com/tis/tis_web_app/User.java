package com.tis.tis_web_app;


public class User {
    private int id;
    private String first_name;
    private String patronymic;
    private String last_name;
    private String password;
    public Role role = new Role("Гость");

    User(String first_name, String patronymic, String last_name, String birth_date, String phone_number, String password, Role role){
        this.first_name = first_name;
        this.patronymic = patronymic;
        this.last_name = last_name;
        this.password = password;
        this.role = role;
    }

    public void set_first_name(String first_name){
        this.first_name = first_name;
    }

    public void set_patronymic(String patronymic){
        this.patronymic = patronymic;
    }

    public void set_last_name(String last_name){
        this.last_name = last_name;
    }

    public void set_role(Role role){
        this.role = role;
    }

    public Integer get_id(){
        return id;
    }

    public String get_first_name(){
        return first_name;
    }

    public String get_patronymic(){
        return patronymic;
    }

    public String get_last_name(){
        return last_name;
    }

    public Role get_role(){
        return role;
    }

    public String get_password(){
        return password;
    }

    public String toString(){
        return "id: " + id + ", first_name: " + first_name + ", patronymic: " + patronymic + "password: "+ password + "role: " + role.get_name();
    }
}

