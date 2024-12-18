package com.tis.tis_web_app;

public class Task {
    private int id;
    private String question;
    private String answer;

    Task(String question, String answer){
        this.question = question;
        this.answer = answer;
    }

    public int get_id(){
        return id;
    }

    public String get_question(){
        return question;
    }

    public String get_answer(){
        return answer;
    }
}
