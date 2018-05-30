package com.example.jesper.myapplication;

import java.io.Serializable;

//Simple player object
public class Player implements Serializable{
    String name;
    int birthYear;
    int birthMonth;

    public Player(String name, int birthYear, int birthMonth) {
        this.name = name;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
    }
}
