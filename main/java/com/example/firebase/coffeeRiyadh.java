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

public class coffeeRiyadh extends AppCompatActivity {

    ViewGroup tcontainer;
    TextView txt, magic;
    Button y, t,s ,button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_riyadh);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Cafes");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tcontainer = findViewById(R.id.conttt);
        txt = findViewById(R.id.textView3);
        y=findViewById(R.id.y);
        button=findViewById(R.id.button);
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
                button.setVisibility(visible? View.VISIBLE: View.GONE);
                t.setVisibility(visible? View.VISIBLE: View.GONE);
                s.setVisibility(visible? View.VISIBLE: View.GONE);


            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "we will do it letar", Toast.LENGTH_SHORT).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "we will do it letar", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void call(View V){
        String a = "0114709658";
        String url = "tel:" + a;
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);startActivity(it);
    }
    public void showLoc(View V){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?"
                +"daddr=24.7713592, 46.7200329"))        ;
        startActivity(intent);}


}

