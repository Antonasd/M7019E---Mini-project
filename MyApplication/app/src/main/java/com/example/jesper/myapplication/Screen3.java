package com.example.jesper.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Screen3 extends AppCompatActivity {

    Button registerTeam1 = null;
    Button registerTeam2 = null;
    Button submitResults = null;

    Boolean team1Checked = false;
    Boolean team2Checked = false;

    ArrayList<Player> team1Lineup = null;
    ArrayList<Player> team2Lineup = null;

    String matchcode = null;
    String time = null;
    String url1 = null;
    String url2 = null;
    String group = null;
    String team1 = null;
    String team2 = null;

    private View.OnClickListener matchSelectListener;

    TableLayout tl = null;

    TableRow previouslyChecked = null;

    Parser parser = null;

    HashMap<String, String> hmap;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3);

        TextView header = (TextView) findViewById(R.id.header);
        final String field = getIntent().getStringExtra("FIELD_SELECTED");
        final String email = getIntent().getStringExtra("ADMIN_EMAIL");
        final String admin_override = getIntent().getStringExtra("ADMIN_OVERRIDE");
        final String year = getIntent().getStringExtra("DATE");
        String year_last2 = year.substring(Math.max(year.length() - 2, 0));
        final String parseString = getIntent().getStringExtra("PARSE_STRING_FIELD");

        header.setText("All matches on field " + field);

        registerTeam1 = findViewById(R.id.button1);
        registerTeam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Screen4.class);
                i.putExtra("Team", "1");
                i.putExtra("ADMIN_OVERRIDE", admin_override);
                i.putExtra("YEAR", year);
                i.putExtra("GROUP", group);
                i.putExtra("URL", "http://teamplaycup.se/cup/"+url1);
                startActivityForResult(i, 1);
            }
        });

        registerTeam2 = findViewById(R.id.button2);
        registerTeam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Screen4.class);
                i.putExtra("Team", "2");
                i.putExtra("ADMIN_OVERRIDE", admin_override);
                i.putExtra("YEAR", year);
                i.putExtra("GROUP", group);
                i.putExtra("URL", "http://teamplaycup.se/cup/"+url2);
                startActivityForResult(i, 1);
            }
        });

        hmap = new HashMap<String, String>();

        setListeners();

        submitResults = (Button) findViewById(R.id.button3);
        submitResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Screen5.class);
                i.putExtra("MATCH_CODE", matchcode);
                i.putExtra("TIME", time);
                i.putExtra("GROUP", group);
                i.putExtra("EMAIL", email);
                i.putExtra("TEAMNAME1", team1);
                i.putExtra("TEAMNAME2", team2);
                i.putExtra("LINEUP1", team1Lineup);
                i.putExtra("LINEUP2", team2Lineup);
                startActivity(i);
            }
        });
        registerTeam1.setEnabled(false);
        registerTeam2.setEnabled(false);
        submitResults.setEnabled(false);
        tl = findViewById(R.id.match_table);

        parser = new Parser();
        try {
            for (ArrayList<String> matches : parser.getMatches(year_last2, parseString)) {
                addMatch(matches.get(1), matches.get(2), matches.get(3), matches.get(4), matches.get(5), matches.get(6), matches.get(7));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void setListeners(){
        matchSelectListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView n = (TextView)(((ViewGroup)view).getChildAt(0));
                TextView g = (TextView)(((ViewGroup)view).getChildAt(1));
                TextView t = (TextView)(((ViewGroup)view).getChildAt(2));
                CheckedTextView checkedText = (CheckedTextView) (((ViewGroup)view).getChildAt(3));
                if(previouslyChecked == null){
                    previouslyChecked = (TableRow) view;
                    checkedText.setChecked(true);
                    checkedText.setCheckMarkDrawable(R.drawable.ic_checkmark);
                    String teamString = (String) checkedText.getText();
                    String[] splitString = teamString.split("\n");
                    team1 = splitString[0];
                    team2 = splitString[1];
                    registerTeam1.setText("Register" + "\n" + team1);
                    registerTeam2.setText("Register" + "\n" + team2);
                    matchcode = (String) n.getText();
                    group = (String) g.getText();
                    time = (String) t.getText();
                    url1 = hmap.get(team1);
                    url2 = hmap.get(team2);
                    registerTeam1.setEnabled(true);
                    registerTeam2.setEnabled(true);
                    team1Checked = false;
                    team2Checked = false;
                    team1Lineup = null;
                    team2Lineup = null;
                } else {
                    CheckedTextView previous = (CheckedTextView) previouslyChecked.getChildAt(3);
                    previous.setChecked(false);
                    previous.setCheckMarkDrawable(0);
                    previouslyChecked = (TableRow) view;
                    CheckedTextView newCheck = (CheckedTextView) previouslyChecked.getChildAt(3);
                    newCheck.setChecked(true);
                    newCheck.setCheckMarkDrawable(R.drawable.ic_checkmark);
                    String teamString = (String) newCheck.getText();
                    String[] splitString = teamString.split("\n");
                    team1 = splitString[0];
                    team2 = splitString[1];
                    registerTeam1.setText("Register" + "\n" + team1);
                    registerTeam2.setText("Register" + "\n" + team2);
                    matchcode = (String) n.getText();
                    group = (String) g.getText();
                    time = (String) t.getText();
                    url1 = hmap.get(team1);
                    url2 = hmap.get(team2);
                    registerTeam1.setEnabled(true);
                    registerTeam2.setEnabled(true);
                    team1Checked = false;
                    team2Checked = false;
                    team1Lineup = null;
                    team2Lineup = null;
                }
            }
        };
    }

    private void addMatch(String number, String group, String time, String team1name, String team1url, String team2name, String team2url){
        TableRow newMatch = new TableRow(this);
        TextView textNumber = new TextView(this);
        TextView textGroup = new TextView(this);
        TextView textTime = new TextView(this);
        CheckedTextView textTeams = new CheckedTextView(this);

        hmap.put(team1name, team1url);
        hmap.put(team2name, team2url);

        newMatch.setMinimumHeight(dpToPx(50));
        newMatch.addView(textNumber);
        newMatch.addView(textGroup);
        newMatch.addView(textTime);
        newMatch.addView(textTeams);

        textNumber.setText(number);
        textNumber.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        textNumber.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT, 0.2f));

        textGroup.setText(group);
        textGroup.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        textGroup.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT,0.40f));

        textTime.setText(time);
        textTime.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        textTime.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT,0.6f));

        textTeams.setText(team1name + "\n" + team2name);
        textTeams.setChecked(false);
        textTeams.setGravity(Gravity.CENTER_VERTICAL);
        textTeams.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT,0.9f));

        newMatch.setOnClickListener(matchSelectListener);
        tl.addView(newMatch);
    }

    private int dpToPx(int dp) {
        float density = this.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ArrayList<Player> returnData = (ArrayList<Player>) data.getSerializableExtra("PlayerList");
                String t = data.getStringExtra("Team");

                if(t.equals("1")){
                    team1Checked = true;
                    team1Lineup = returnData;
                    registerTeam1.setEnabled(false);
                } else if(t.equals("2")){
                    team2Checked = true;
                    team2Lineup = returnData;
                    registerTeam2.setEnabled(false);
                }

                if(team1Checked && team2Checked){
                    submitResults.setEnabled(true);
                }
            }
        }
    }
}