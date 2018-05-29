package com.example.jesper.myapplication;

import android.app.Application;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Adds a 1 second artificial delay to the splash screen
        // Don't keep this! This is just for demo purposes so cold launches takes some time
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
    }
}
