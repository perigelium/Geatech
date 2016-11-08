package com.example.zubcu.geatech;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by zubcu on 9/27/2016.
 */

public class Meniu extends AppCompatActivity {

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;



//        ActionBar actionBar = getActionBar();
//        getSupportActionBar().setTitle("Geatech");

        //actionBar.setIcon(R.drawable.geatech_green);
    }



}
