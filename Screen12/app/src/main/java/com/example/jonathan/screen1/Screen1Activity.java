package com.example.jonathan.screen1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.text.TextUtils;
import android.support.design.widget.Snackbar;
import android.widget.TextView;
import android.text.TextWatcher;
import java.util.Calendar;



public class Screen1Activity extends AppCompatActivity {
    EditText dateField;
    EditText pass1Field;
    EditText pass2Field;
    EditText email1Field;
    EditText email2Field;
    Button letsGoButton;
    String adminCode = "1234";
    boolean codesOK = false;
    boolean emailsOK = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);
        dateField = (EditText) findViewById(R.id.date);
        pass1Field = (EditText) findViewById(R.id.pass1);
        pass2Field = (EditText) findViewById(R.id.pass2);
        email1Field = (EditText) findViewById(R.id.email1);
        email2Field = (EditText) findViewById(R.id.email2);
        letsGoButton = (Button) findViewById(R.id.letsGo);


        letsGoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkCodes(pass1Field.getText().toString(), pass2Field.getText().toString());
                    validateEmail(email1Field.getText().toString(),email2Field.getText().toString());
                    if(codesOK && emailsOK){
                        System.out.println("Working.");
                        if(dateField.getText().toString().isEmpty()){
                            dateField.setError("Enter a date");
                        }
                        else{
                            //todo

                        }

                    }
                    else{
                        codesOK = false;
                        emailsOK = false;
                        System.out.println("Not working.");
                    }

                }
            }
        );

    }

    private void checkCodes(String pass1, String pass2 ){
        if (pass1.equals(pass2) && pass1.equals(adminCode)){
            codesOK = true;
            System.out.println(codesOK);

        }
        else{
            codesOK = false;
            pass1Field.setError("Wrong code or doesn't match");
            pass2Field.setError("Wrong code or doesn't match");
            pass1Field.setText("");
            pass2Field.setText("");

        }
    }

    private void validateEmail(String email1, String email2){
        if(isValidEmail(email1) && email1.equals(email2)){
            emailsOK = true;
            System.out.println(emailsOK);
        }
        else{
            emailsOK = false;
            email1Field.setError("Not valid email or doesn't match");
            email2Field.setError("Not valid email or doesn't match");
            email1Field.setText("");
            email2Field.setText("");
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
