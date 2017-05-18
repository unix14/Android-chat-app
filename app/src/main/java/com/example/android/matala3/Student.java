package com.example.android.matala3;

/**
 * Created by eyal on 28-Apr-17.
 */

public class Student {
    String title;
    String description;
    int image;
    Boolean checkBox;
    String date;
    String time;
    Student(){

    }
    Student(String title,String desc, int image,Boolean cb,String date,String time){
        this.title = title;
        this.description = desc;
        this.image = image;
        this.checkBox = cb;
        this.date = date;
        this.time = time;
    }
}
