package com.msiSpringchat.springchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msiSpringchat.springchat.Adapter.UserAdapter;
import com.msiSpringchat.springchat.Holder.UserHolder;
import com.msiSpringchat.springchat.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    ArrayList<UserHolder> users;
    UserAdapter userAdapter;
    TextView cuUserName;
    androidx.appcompat.widget.Toolbar toolbar;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);





        firebaseAuth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();

        users = new ArrayList<>();
        userAdapter = new UserAdapter(HomeActivity.this,users);
        recyclerView.setAdapter(userAdapter);

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    UserHolder user = snapshot1.getValue(UserHolder.class);


                    String uid = firebaseAuth.getUid();
                    if(!user.getUid().equals(uid))
                        users.add(user);






                }
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Please Provide Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Profile is clicked", Toast.LENGTH_SHORT).show();

                break;

            case R.id.settings:
                Toast.makeText(getApplicationContext(), "Seating is clicked", Toast.LENGTH_SHORT).show();
                break;

        }


        return true;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);


        return true;
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