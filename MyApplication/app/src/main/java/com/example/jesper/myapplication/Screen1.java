package com.example.jesper.myapplication;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.text.TextUtils;

import java.util.Calendar;


public class Screen1 extends AppCompatActivity {
    EditText dateField;
    EditText pass1Field;
    EditText pass2Field;
    EditText email1Field;
    EditText email2Field;
    TextInputLayout dateLayout;
    TextInputLayout pass1Layout;
    TextInputLayout pass2Layout;
    TextInputLayout email1Layout;
    TextInputLayout email2Layout;
    Button letsGoButton;
    String adminCode = "1234"; //Static admin code for testing purposes
    boolean codesOK = false;
    boolean emailsOK = false;
    boolean dateOK = false;
    String adminOverride = "false";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);
        dateField = (EditText) findViewById(R.id.date);
        dateLayout = (TextInputLayout) findViewById(R.id.textinputdate);
        pass1Field = (EditText) findViewById(R.id.pass1);
        pass1Layout = (TextInputLayout) findViewById(R.id.textinputadmin);
        pass2Field = (EditText) findViewById(R.id.pass2);
        pass2Layout = (TextInputLayout) findViewById(R.id.textinputadmin2);
        email1Field = (EditText) findViewById(R.id.email1);
        email1Layout = (TextInputLayout) findViewById(R.id.textinputemail);
        email2Field = (EditText) findViewById(R.id.email2);
        email2Layout = (TextInputLayout) findViewById(R.id.textinputemail2);
        letsGoButton = (Button) findViewById(R.id.letsGo);

        //OnClick listener for Screen1 -> Screen2 Transition
        letsGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCodes(pass1Field.getText().toString(), pass2Field.getText().toString());
                validateEmail(email1Field.getText().toString(),email2Field.getText().toString());
                checkDate(dateField.getText().toString());
                //If Codes, Emails, Dates inputs are flagged as OK, proceed to Screen 2
                if(codesOK && emailsOK && dateOK){
                    Intent i = new Intent(getApplicationContext(), Screen2.class);
                    i.putExtra("DATE", dateField.getText().toString());
                    i.putExtra("ADMIN_EMAIL", email1Field.getText().toString());
                    i.putExtra("ADMIN_OVERRIDE", adminOverride);
                    startActivity(i);
                } else{
                    codesOK = false;
                    emailsOK = false;
                    dateOK = false;
                    adminOverride = "false";
                }
            }
        });

        //Add TextChangedListeners to dynamically remove errors after user starts typing a new input
        //This makes the UI feel more responsive
        dateField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                dateLayout.setError(null);
            }
        });
        pass1Field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > pass1Layout.getCounterMaxLength())
                    pass1Layout.setError("Code cannot be longer than " + pass1Layout.getCounterMaxLength() + " digits");
                else
                    pass1Layout.setError(null);
            }
        });
        pass2Field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > pass2Layout.getCounterMaxLength())
                    pass2Layout.setError("Code cannot be longer than " + pass2Layout.getCounterMaxLength() + " digits");
                else
                    pass2Layout.setError(null);
            }
        });
        email1Field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                email1Layout.setError(null);
            }
        });
        email2Field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                email2Layout.setError(null);
            }
        });
    }

    private void checkCodes(String pass1, String pass2 ){
        //Codes are equal and match admin code, flag as OK
        if (pass1.equals(pass2) && pass1.equals(adminCode)){
            codesOK = true;
            adminOverride = "true";
            pass1Layout.setError(null);
            pass2Layout.setError(null);

        }
        //Codes are equal but do not match the admin code
        else if (!pass1.isEmpty() && pass1.equals(pass2) && !pass1.equals(adminCode)) {
            codesOK = false;
            adminOverride = "false";
            pass1Field.setText("");
            pass2Field.setText("");
            pass1Layout.setError("Invalid code selected");
            pass2Layout.setError("Invalid code selected");
        }
        //Code2 entered but Code1 empty, Invalid input
        else if (pass1.isEmpty() && !pass2.isEmpty()) {
            codesOK = false;
            adminOverride = "false";
            pass1Field.setText("");
            pass1Layout.setError("Please enter a 4 digit code");
        }
        //Code1 entered but Code2 empty, Invalid input
        else if (pass2.isEmpty() && !pass1.isEmpty()){
            codesOK = false;
            adminOverride = "false";
            pass2Field.setText("");
            pass2Layout.setError("Please enter a 4 digit code");
        }
        //Inputs are empty (OK because admin code is not required
        else if (pass1.isEmpty() && pass2.isEmpty()){
            codesOK = true;
            adminOverride = "false";
        } else{
        //Inputs don't match
            codesOK = false;
            adminOverride = "false";
            pass1Field.setText("");
            pass2Field.setText("");
            pass1Layout.setError("Codes don't match");
            pass2Layout.setError("Codes don't match");

        }
    }

    private void validateEmail(String email1, String email2){
        //Valid input, flag as OK
        if(isValidEmail(email1) && email1.equals(email2)){
            emailsOK = true;
            email1Layout.setError(null);
            email2Layout.setError(null);
        }
        //Inputs don't match
        else if(!email1.equals(email2)){
            emailsOK = false;
            email1Field.setText("");
            email2Field.setText("");
            email1Layout.setError("Emails don't match");
            email2Layout.setError("Emails don't match");
        } else{
        //Invalid input
            emailsOK = false;
            email1Field.setText("");
            email2Field.setText("");
            email1Layout.setError("Invalid email selected");
            email2Layout.setError("Invalid email selected");
        }
    }

    private void checkDate(String date){
        //No date selected
        if(date.isEmpty()) {
            dateOK = false;
            dateField.setText("");
            dateLayout.setError("Please enter a year");
        //Date earlier than 2000 or later than the current year (invalid input)
        } else if (Integer.parseInt(date) < 2000 || Integer.parseInt(date) > Calendar.getInstance().get(Calendar.YEAR)){
            dateOK = false;
            dateField.setText("");
            dateLayout.setError("Invalid year selected, please choose a value between 2000 and " + Calendar.getInstance().get(Calendar.YEAR));
        } else {
        //Valid input, flag as OK
            dateOK = true;
            dateLayout.setError(null);
        }
    }

    //Checks for non email characters
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
