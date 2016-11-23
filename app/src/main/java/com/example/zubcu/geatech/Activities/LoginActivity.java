package com.example.zubcu.geatech.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.example.zubcu.geatech.R.*;


public class LoginActivity extends AppCompatActivity {

    Button login;
    Button btAcces;
    Button btPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.login_activity);

        login = (Button) findViewById(id.btLogin);

        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(registerIntent);
            }
        });

        btAcces = (Button)findViewById(id.btAces);
        btAcces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstAccess = new Intent(LoginActivity.this, UserFirstAccess.class);
                startActivity(firstAccess);
            }
        });

        btPassword = (Button)findViewById(id.btPassword);
        btPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PasswordRecover = new Intent(LoginActivity.this, UserPasswordRecover.class);
                startActivity(PasswordRecover);
            }
        });



    }
}
