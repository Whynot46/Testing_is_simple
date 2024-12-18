package com.tis.tis_web_app;

public class TeacherProfile {
    private int id;
    private int user_id;

    TeacherProfile(int id, int user_id){
        this.id = id;
        this.user_id = user_id;
    }

    public int get_id(){
        return id;
    }

    public int get_user_id(){
        return user_id;
    }

}
