package com.tis.tis_web_app;

public class Task {
    private int id;
    private String question;
    private String answer;

    Task(int id, String question, String answer){
        this.id = id;
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
