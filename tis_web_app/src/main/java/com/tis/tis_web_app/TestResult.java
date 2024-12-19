package com.tis.tis_web_app;

public class TestResult {
    private int id;
    private int test_id;
    private int points;

    TestResult(int id, int test_id, int points){
        this.id = id;
        this.test_id = test_id;
        this.points = points;
    }

    public int get_id(){
        return id;
    }

    public int get_test_id(){
        return test_id;
    }

    public int get_points(){
        return points;
    }
}
