package com.tis.tis_web_app;

import java.util.ArrayList;

public class Test {
    private int id;
    private int topic_id;
    private ArrayList<Integer> tasks_id = new ArrayList<Integer>();
    private int teacher_id;


    Test(ArrayList<Integer> tasks_id, int teacher_id){
        this.tasks_id = tasks_id;
        this.teacher_id = teacher_id;
    }

    public int get_id(){
        return id;
    }

    public int get_topic_id(){
        return topic_id;
    }

    public ArrayList<Integer> get_tasks_id(){
        return tasks_id;
    }

    public int get_teacher_id(){
        return teacher_id;
    }

}