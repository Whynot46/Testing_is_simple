package com.tis.tis_web_app;

import java.util.ArrayList;

public class TeacherProfile {
    private int id;
    private int user_id;
    private ArrayList<Integer> topics_id;

    TeacherProfile(int id, int user_id, ArrayList<Integer> topics_id){
        this.id = id;
        this.user_id = user_id;
        this.topics_id = topics_id;
    }

    public int get_id(){
        return id;
    }

    public int get_user_id(){
        return user_id;
    }

    public ArrayList<Integer> get_topics_id(){
        return topics_id;
    }

}
