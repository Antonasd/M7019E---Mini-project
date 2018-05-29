package com.example.jesper.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Screen2 extends AppCompatActivity implements View.OnClickListener{

    public String buttonPressed = null;
    public String parse_Field = null;
    TextView out = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        out = (TextView) findViewById(R.id.output);

        // Initialize All Buttons
        Button a11 = (Button) findViewById(R.id.A11);
        a11.setOnClickListener(this);
        Button b11 = (Button) findViewById(R.id.B11);
        b11.setOnClickListener(this);
        Button c11 = (Button) findViewById(R.id.C11);
        c11.setOnClickListener(this);
        Button c1_7 = (Button) findViewById(R.id.C1_7);
        c1_7.setOnClickListener(this);
        Button c2_7 = (Button) findViewById(R.id.C2_7);
        c2_7.setOnClickListener(this);
        Button c3_5 = (Button) findViewById(R.id.C3_5);
        c3_5.setOnClickListener(this);
        Button c4_5 = (Button) findViewById(R.id.C4_5);
        c4_5.setOnClickListener(this);
        Button d_7 = (Button) findViewById(R.id.D_7);
        d_7.setOnClickListener(this);
        Button e1 = (Button) findViewById(R.id.E1);
        e1.setOnClickListener(this);
        Button e2 = (Button) findViewById(R.id.E2);
        e2.setOnClickListener(this);
        Button e3 = (Button) findViewById(R.id.E3);
        e3.setOnClickListener(this);
        Button s1 = (Button) findViewById(R.id.S1);
        s1.setOnClickListener(this);
        Button r1 = (Button) findViewById(R.id.R1);
        r1.setOnClickListener(this);
        Button r2 = (Button) findViewById(R.id.R2);
        r2.setOnClickListener(this);
        Button r3 = (Button) findViewById(R.id.R3);
        r3.setOnClickListener(this);
        Button r4 = (Button) findViewById(R.id.R4);
        r4.setOnClickListener(this);
        Button next = (Button) findViewById(R.id.NextActivity);
        next.setOnClickListener(this);
    }

    // Switch for onclick on each button, changes the buttonPressed and the parse string (for the HTML parser) to send to screen 3
    // As well as updates the TextView text
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.A11:
                buttonPressed = "A-11";
                parse_Field = "arena=A 11-manna (Gstad)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.B11:
                buttonPressed = "B-11";
                parse_Field = "arena=B 11-manna (Gstad)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.C11:
                buttonPressed = "C-11";
                parse_Field = "arena=C 11-manna (Byavallen)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.C1_7:
                buttonPressed = "C1-7";
                parse_Field = "arena=C1 7-manna (Gstad)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.C2_7:
                buttonPressed = "C2-7";
                parse_Field = "arena=C2 7-manna (Gstad)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.C3_5:
                buttonPressed = "C3-5";
                parse_Field = "arena=C3 5-manna (Gstad)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.C4_5:
                buttonPressed = "C4-5";
                parse_Field = "arena=C4 5-manna (Gstad)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.D_7:
                buttonPressed = "D-7";
                parse_Field = "arena=D 7-manna (Gstad)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.E1:
                buttonPressed = "E1-7";
                parse_Field = "arena=E1 7-manna (Gstad)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.E2:
                buttonPressed = "E2-5";
                parse_Field = "arena=E2 5-manna (Gstad)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.E3:
                buttonPressed = "E3-5";
                parse_Field = "arena=E3 5-manna (Gstad)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.S1:
                buttonPressed = "S1-11";
                parse_Field = "arena=S1 11-manna (Sunderbyn)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.R1:
                buttonPressed = "R1-5";
                parse_Field = "arena=R1 5-manna (Rutvik)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.R2:
                buttonPressed = "R2-5";
                parse_Field = "arena=R2 5-manna (Rutvik)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.R3:
                buttonPressed = "R3-7";
                parse_Field = "arena=R3 7-manna (Rutvik)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.R4:
                buttonPressed = "R4-7";
                parse_Field = "arena=R4 7-manna (Rutvik)";
                out.setText("Field selected:  " + buttonPressed);
                break;

            case R.id.NextActivity:
                if(buttonPressed != null) {
                    // If any button has been pressed, the continue button leads you to Screen 3
                    // And sends through all necessary data
                    Intent i = new Intent(this, Screen3.class);
                    String date = getIntent().getStringExtra("DATE");
                    String email = getIntent().getStringExtra("ADMIN_EMAIL");
                    String adminOverride = getIntent().getStringExtra("ADMIN_OVERRIDE");
                    i.putExtra("FIELD_SELECTED", buttonPressed);
                    i.putExtra("PARSE_STRING_FIELD", parse_Field);
                    i.putExtra("DATE", date);
                    i.putExtra("ADMIN_EMAIL", email);
                    i.putExtra("ADMIN_OVERRIDE", adminOverride);
                    startActivity(i);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please select a field.", Toast.LENGTH_SHORT);
                    toast.show();
                }

            default:
                break;
        }
    }
}
