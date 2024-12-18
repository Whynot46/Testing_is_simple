package com.tis.tis_web_app;

public class AdminProfile {
    private int id;
    private int user_id;

    AdminProfile(int id, int user_id){
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
