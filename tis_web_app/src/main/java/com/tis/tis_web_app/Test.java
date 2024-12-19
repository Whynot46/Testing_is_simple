package com.tis.tis_web_app;

import java.util.ArrayList;

public class Test {
    private int id;
    private int topic_id;
    private ArrayList<Task> tasks;
    private int teacher_id;


    Test(int id, int topic_id, ArrayList<Task> tasks, int teacher_id){
        this.id = id;
        this.topic_id = topic_id;
        this.tasks = tasks;
        this.teacher_id = teacher_id;
    }

    public int get_id(){
        return id;
    }

    public int get_topic_id(){
        return topic_id;
    }

    public ArrayList<Task> get_tasks(){
        return tasks;
    }

    public int get_teacher_id(){
        return teacher_id;
    }

}