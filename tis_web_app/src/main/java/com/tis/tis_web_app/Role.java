package com.tis.tis_web_app;

public class Role {
    private int id;
    private String name;

    Role(String name){
        this.name = name;
    }

    public int get_id(String name){
        return id;
    }

    public String get_name(){
        return name;
    }
}