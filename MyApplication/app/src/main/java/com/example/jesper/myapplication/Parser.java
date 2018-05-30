package com.example.jesper.myapplication;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private Document doc = null;
    private boolean done = false;
    private boolean error = false;

	//"Config" for the URLs where data is to be fetched from.
    private String getURL (String year, String value, boolean forTeam) {
        if(forTeam) {
            return "http://teamplaycup.se/cup/?games&home=kurirenspelen/" + year + "&scope=all&" + value + "&field=";
        } else {
            return "http://teamplaycup.se/cup/?overviewgroup&home=kurirenspelen/" + year + "&scope=" + value;
        }
    }

    //Get all matches for the specified year and field/arena, used in screen 3.
    public ArrayList<ArrayList<String>> getMatches(final String year, final String arena) throws IOException {
		
		//Fetching from a URL shouldn't be run in the main thread.
        Thread getThread = new Thread() {
            public void run(){
                doc = null;

                try {
                    doc = Jsoup.connect(getURL(year, arena, true)).get();
                    done = true;
                }
                catch(IOException e){
                    error  = true;
                }


            }
        };
        getThread.start();

        while (!done) {
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            if(error){
                error = false;
                throw new IOException();
            }
        }

        done = false;

        Elements titles = doc.getElementsByTag("h4");
        Elements tables = doc.getElementsByTag("tbody");

        ArrayList<ArrayList<String>> games = new ArrayList<ArrayList<String>>();

        int index = 0;
	//There is one header with a corresponding table for each day of the season, let's loop through them and get every match.
        for (Element title : titles) {

            Elements curGames = tables.get(index).children();

            for (Element curGame : curGames) {
                ArrayList<String> match = new ArrayList<String>();
                Elements attr = curGame.children();

                match.add(title.text()); //Date
                match.add(attr.get(0).children().get(0).text()); //Match number
                match.add(attr.get(1).children().get(0).text()); //Group
                match.add(attr.get(2).text()); //Time
                match.add(attr.get(3).children().get(0).text()); //Team 1 name
                match.add(attr.get(3).children().get(0).attr("href")); //Team 1 link
                match.add(attr.get(3).children().get(1).text()); //Team 2 name
                match.add(attr.get(3).children().get(1).attr("href")); //Team 2 link

                games.add(match);
            }



            index++;
        }

        return games;
    }

   //Get a list of all the players in a specific match, used in screen 4.
    public ArrayList<ArrayList<String>> getPlayers(final String url) throws IOException {

        Thread getThread = new Thread() {
            public void run() {
                doc = null;

                try {
                    doc = Jsoup.connect(url).maxBodySize(Integer.MAX_VALUE).get();
                    done = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    error = true;
                }

            }
        };
        getThread.start();

        while (!done) {
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            if(error){
                error = false;
                throw new IOException();
            }
        }

        done=false;

		//The player's table is always the second table.
        Element playersHtml = doc.getElementsByTag("tbody").get(1);
        ArrayList<ArrayList<String>> players = new ArrayList<ArrayList<String>>();

        for(Element player : playersHtml.children()){
            ArrayList<String> newPlayer = new ArrayList<>();
            newPlayer.add(player.child(0).text()); //Name
            newPlayer.add(player.child(1).text()); //Age
            players.add(newPlayer);
        }

        return players;
    }

    //Get the year of birth for a specific group, used to determine if players are over aged.
    public String getAge(final String year, final String group) throws IOException {

        Thread getThread = new Thread() {
            public void run() {
                doc = null;

                try {
                    doc = Jsoup.connect(getURL(year, group, false)).get();
                    done = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    error = true;
                }

            }
        };
        getThread.start();

        while (!done) {
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            if(error){
                error = false;
                throw new IOException();
            }
        }
        done = false;

        String groupString = doc.getElementsByTag("h2").get(0).text();

        String age;
	//Use the first letter to determine if the group is of boys or girls, then get the year of birth.
        if(groupString.substring(0,1).equals("P")) {
            age = groupString.substring(7,9);
        } else {
            age = groupString.substring(8,10);
        }

        return age;
    }
}
