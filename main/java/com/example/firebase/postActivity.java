package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.firebase.adapters.AdapterPosts;
import com.example.firebase.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class postActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);




        actionBar = getSupportActionBar();
        actionBar.setTitle("comment");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
            firebaseAuth = FirebaseAuth.getInstance();
            recyclerView = findViewById(R.id.postRecyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);

            layoutManager.setStackFromEnd(true);
            layoutManager.setReverseLayout(true);

            recyclerView.setLayoutManager(layoutManager);

            postList = new ArrayList<>();
            loadPosts();


        }
        private void loadPosts() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postList.clear();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        ModelPost modelPost =ds.getValue(ModelPost.class);
                        postList.add(modelPost);

                        adapterPosts = new AdapterPosts(postActivity.this,postList);
                        recyclerView.setAdapter(adapterPosts);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void checkUserStatus() {

            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
            } else {
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
            }

        }


    }

