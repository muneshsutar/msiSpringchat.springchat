package com.msiSpringchat.springchat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.msiSpringchat.springchat.R;

public class CuserActivity extends AppCompatActivity {

    ImageView imageView;
    TextView cUsserName;
    FirebaseDatabase database;
    de.hdodenhof.circleimageview.CircleImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuser);
        imageView = findViewById(R.id.imageView);
        cUsserName = findViewById(R.id.cUsserName);
        profile = findViewById(R.id.profile);

        database =FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        String profiles = intent.getStringExtra("Photo");
        String cuSERnAME = intent.getStringExtra("name");
        cUsserName.setText(cuSERnAME);


        Glide.with(CuserActivity.this).load(profiles)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(imageView);

        Glide.with(CuserActivity.this).load(profiles)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(profile);



    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }
}