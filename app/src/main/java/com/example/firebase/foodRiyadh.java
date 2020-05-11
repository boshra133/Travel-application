package com.example.firebase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class foodRiyadh extends AppCompatActivity {
    ViewGroup tcontainer;
    TextView txt, magic;

    Button y, c,t,s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_riyadh);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Restaurants");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        tcontainer = findViewById(R.id.conttt);
        txt = findViewById(R.id.textView3);

        y=findViewById(R.id.y);
        c=findViewById(R.id.button);
        t=findViewById(R.id.t);
        s=findViewById(R.id.s);

        magic = findViewById(R.id.textViewn);
        magic.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override

            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(tcontainer);
                visible =!visible;
                txt.setVisibility(visible? View.VISIBLE: View.GONE);
                y.setVisibility(visible? View.VISIBLE: View.GONE);
                c.setVisibility(visible? View.VISIBLE: View.GONE);
                t.setVisibility(visible? View.VISIBLE: View.GONE);
                s.setVisibility(visible? View.VISIBLE: View.GONE);


            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(com.example.firebase.foodJeddah.this,addPostActivity.class));
                Toast.makeText(getApplicationContext(), "we will do it letar", Toast.LENGTH_SHORT).show();
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "we will do it letar", Toast.LENGTH_SHORT).show();                    }
        });

    }
    public void call(View V){
        String a = "0115203531";
        String url = "tel:" + a;
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);startActivity(it);
    }
    public void showLoc(View V){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?"
                +"daddr=24.7786123, 46.7343982"))        ;
        startActivity(intent);}



}
