package com.tis.tis_web_app;

public class UserStatistic {
    private int user_id;
    private double average_points;
    private float highest_points;
    private float lowest_points;

    UserStatistic(int user_id, double average_points, float highest_points, float lowest_points){
        this.user_id = user_id;
        this.average_points = average_points;
        this.highest_points = highest_points;
        this.lowest_points = lowest_points;
    }

    public int get_user_id(){
        return user_id;
    }

    public float get_highest_points(){
        return highest_points;
    }

    public double get_average_points(){
        return average_points;
    }

    public float get_lowest_points(){
        return lowest_points;
    }

}
