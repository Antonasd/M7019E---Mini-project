package com.example.jesper.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    // Creates a empty activity with a background splash image that instantly calls Screen1 whenever it is done
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, Screen1.class);
        startActivity(intent);
        finish();
    }
}
