package com.tis.tis_web_app;

import java.util.ArrayList;

public class StudentProfile {
    private int id;
    private int user_id;
    private ArrayList<Integer> test_results_id = new ArrayList<Integer>();

    StudentProfile(int id, int user_id, ArrayList<Integer> test_results_id){
        this.id = id;
        this.user_id = user_id;
        this.test_results_id = test_results_id;
    }

    public int get_id(){
        return id;
    }

    public int get_user_id(){
        return user_id;
    }

    public ArrayList<Integer> get_test_results_id(){
        return test_results_id;
    }

}
