package com.tis.tis_web_app;

public class Topic {
    private int id;
    private String name;

    Topic(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int get_id(String name){
        return id;
    }

    public String get_name(){
        return name;
    }
}