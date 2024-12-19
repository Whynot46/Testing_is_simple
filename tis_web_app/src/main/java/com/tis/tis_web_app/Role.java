package com.tis.tis_web_app;


public class Role {
    private int id;
    private String name;

    Role(int id, String name){
        this.id = id;
        this.name = name;
    }

    Role(int id){
        this.id = id;
        this.name = DataBase.get_role_name(id);
    }

    public int get_id(){
        return id;
    }

    public String get_name(){
        return name;
    }
}
