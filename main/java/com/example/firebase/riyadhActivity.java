package com.example.firebase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class riyadhActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riyadh);


        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Riyadh");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

    }
    public void moveTocoff(View V){

        Intent i = new Intent(getApplicationContext(),coffeeRiyadh.class);
        startActivityForResult(i,1); // 1: an arbitrary value
    }


    public void moveTofood(View V){

        Intent i = new Intent(getApplicationContext(),foodRiyadh.class);
        startActivityForResult(i,1); // 1: an arbitrary value
    }


    public void moveToshop(View V){

        Intent i = new Intent(getApplicationContext(),shopingRiyadh.class);
        startActivityForResult(i,1); // 1: an arbitrary value
    }
    }

