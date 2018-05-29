package com.example.jesper.myapplication;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Screen6 extends AppCompatActivity{

    TextView out_field = null;
    TextView out_date = null;
    TextView out_email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen6);

        out_field = (TextView) findViewById(R.id.output);
        out_date = (TextView) findViewById(R.id.date);
        out_email = (TextView) findViewById(R.id.email);
        String field = getIntent().getStringExtra("FIELD_SELECTED");
        String date = getIntent().getStringExtra("DATE");
        final String email = getIntent().getStringExtra("ADMIN_EMAIL");
        String adminOverride = getIntent().getStringExtra("ADMIN_OVERRIDE");
        out_field.setText(field);
        out_date.setText(date);
        out_email.setText(email + "\n" + adminOverride);
        Button button = (Button) findViewById(R.id.NextActivity);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            // When the user clicks the button:
            public void onClick(View v) {
                try {
                    // Write a dummy text file to this application's internal
                    // cache dir.
                    Utils.createCachedFile(Screen6.this,
                            "Test.txt", "This is a test");

                    // Then launch the activity to send that file via gmail.
                    startActivity(Utils.getSendEmailIntent(
                            Screen6.this,
                            email, "Gammelstads IF - Match Result", //TODO Pass teams here?
                            "See attached", "Test.txt"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Catch if Gmail is not available on this device
                catch (ActivityNotFoundException e) {
                    Toast.makeText(Screen6.this,
                            "Gmail is not available on this device.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
