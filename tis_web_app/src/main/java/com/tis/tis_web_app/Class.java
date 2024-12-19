package com.tis.tis_web_app;

import java.util.ArrayList;

public class Class {
    private int id;
    private String name;
    private ArrayList<Integer> students_id;
    private int teacher_id;

    Class(int id, String name, ArrayList<Integer> students_id, int teacher_id){
        this.id = id;
        this.name = name;
        this.students_id = students_id;
        this.teacher_id = teacher_id;
    }

    public int get_id(){
        return id;
    }

    public String get_name(){
        return name;
    }

    public ArrayList<Integer> get_students_id(){
        return students_id;
    }

    public int get_teacher_id(){
        return teacher_id;
    }

}
