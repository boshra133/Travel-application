package com.example.firebase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManagerNonConfig;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class jeddahActivity extends AppCompatActivity {
    //ImageView coffee;
    @Override
    //private FrameLayout fragmentContainer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeddah);


        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Jeddah");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

    }
    public void moveTocoff(View V){

        Intent i = new Intent(getApplicationContext(),coffeeJeddah.class);
        startActivityForResult(i,1); // 1: an arbitrary value
    }


    public void moveTofood(View V){

        Intent i = new Intent(getApplicationContext(),foodJeddah.class);
        startActivityForResult(i,1); // 1: an arbitrary value
    }


    public void moveToshop(View V){

        Intent i = new Intent(getApplicationContext(),shopingJeddah.class);
        startActivityForResult(i,1); // 1: an arbitrary value
    }
}
