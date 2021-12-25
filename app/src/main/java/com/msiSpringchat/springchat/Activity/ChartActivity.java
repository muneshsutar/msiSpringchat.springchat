package com.msiSpringchat.springchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msiSpringchat.springchat.Adapter.MessageAdapter;
import com.msiSpringchat.springchat.Holder.Message;
import com.msiSpringchat.springchat.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChartActivity extends AppCompatActivity {

    TextView cuUsserName,userstatus;

    LinearLayout gotoCuserProfile;

    MessageAdapter adapter;
    ArrayList<Message> messages;

    ImageView sendBtn;
    EditText messageBox;
    RecyclerView recyclerView;
    de.hdodenhof.circleimageview.CircleImageView profile;




    String senderRoom, receiverRoom;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        cuUsserName =findViewById(R.id.cuUsserName);
        recyclerView = findViewById(R.id.recyclerView);
        profile = findViewById(R.id.profile);

        gotoCuserProfile = findViewById(R.id.gotoCuserProfile);


        sendBtn = findViewById(R.id.sendBtn);

        messageBox = findViewById(R.id.messageBox);
        database = FirebaseDatabase.getInstance();



        userstatus = findViewById(R.id.statusu);


        messages = new ArrayList<>();






        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String receiverUid = intent.getStringExtra("uid");
        String profiles = intent.getStringExtra("image");

        cuUsserName.setText(name);
        Glide.with(ChartActivity.this).load(profiles)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(profile);

        String senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        adapter = new MessageAdapter(ChartActivity.this, messages, senderRoom, receiverRoom);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        database.getReference().child("presence").child(receiverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if (!status.isEmpty()){
                        if (status.equals("Offline")){
                            userstatus.setVisibility(View.GONE);
                        }else {
                            userstatus.setText(status);
                            userstatus.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gotoCuserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),CuserActivity.class);



                intent.putExtra("Photo", profiles);
                intent.putExtra("name",name);


                startActivity(intent);

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageTxt = messageBox.getText().toString();
                Date date = new Date();
                messageBox.setText("");

                Message  message = new Message(messageTxt,senderUid,date.getTime());

                HashMap<String, Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime", date.getTime());
                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);
                if (!messageTxt.isEmpty()){
                    database.getReference().child("chats").child(senderRoom)

                            .child("messages")
                            .push()
                            .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            database.getReference().child("chats").child(receiverRoom)

                                    .child("messages")
                                    .push()
                                    .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });

                        }
                    });


                }else {
                    Toast.makeText(getApplicationContext(), "Please Right A Message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();

                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messages.add(message);

                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        final Handler handler = new Handler();

        messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                database.getReference().child("presence").child(senderUid).setValue("typing...");
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(userStoppedTyping,1000);

            }


            Runnable userStoppedTyping = new Runnable() {
                @Override
                public void run() {
                    database.getReference().child("presence").child(senderUid).setValue("Online");
                }
            };
        });


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