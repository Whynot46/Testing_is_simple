package com.tis.tis_web_app;

public class TestStatistic {
    private int test_id;
    private double average_test_results;
    private float highest_test_results;
    private float lowest_test_results;

    TestStatistic(int test_id, double average_test_results, float highest_test_results, float lowest_test_results){
        this.test_id = test_id;
        this.average_test_results = average_test_results;
        this.highest_test_results = highest_test_results;
        this.lowest_test_results = lowest_test_results;
    }

    public int get_test_id(){
        return test_id;
    }

    public double get_average_test_results(){
        return average_test_results;
    }

    public float get_highest_test_results(){
        return highest_test_results;
    }

    public float get_lowest_test_results(){
        return lowest_test_results;
    }
}
