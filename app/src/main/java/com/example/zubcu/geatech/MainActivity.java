package com.example.zubcu.geatech;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.example.zubcu.geatech.R.*;


public class MainActivity extends AppCompatActivity {

    Button login;
    Button btAces;
    Button btPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        login = (Button) findViewById(id.btLogin);

        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent registerIntent = new Intent(MainActivity.this, UserFrag.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

        btAces = (Button)findViewById(id.btAces);
        btAces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntentr = new Intent(MainActivity.this, PrimoAcceso.class);
                MainActivity.this.startActivity(registerIntentr);
            }
        });

        btPassword = (Button)findViewById(id.btPassword);
        btPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntentr = new Intent(MainActivity.this, RecuperaPassworld.class);
                MainActivity.this.startActivity(registerIntentr);
            }
        });



    }
}
