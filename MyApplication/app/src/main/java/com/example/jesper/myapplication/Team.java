package com.example.jesper.myapplication;
import java.util.ArrayList;

public class Team {
    ArrayList<Player> players;
    String teamName;
    int teamYear;

    public Team(String teamName, int teamYear) {
        this.teamName = teamName;
        this.teamYear = teamYear;
    }

    public Team(String teamName, int teamYear, ArrayList<Player> players) {
        this.teamName = teamName;
        this.teamYear = teamYear;
        this.players = players;
    }
}