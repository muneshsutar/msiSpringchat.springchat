package com.msiSpringchat.springchat.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msiSpringchat.springchat.Activity.ChartActivity;
import com.msiSpringchat.springchat.Holder.UserHolder;
import com.msiSpringchat.springchat.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder> {
    Context context;
    ArrayList<UserHolder> users;


    public UserAdapter(Context context, ArrayList<UserHolder> users) {
        this.context = context;
        this.users = users;


    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list, parent, false);

        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        UserHolder user = users.get(position);

        String senderId = FirebaseAuth.getInstance().getUid();

        String senderRoom = senderId + user.getUid();

        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            long time = snapshot.child("lastMsgTime").getValue(Long.class);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            holder.messageTime.setText(dateFormat.format(new Date(time)));
                            holder.userStatus.setText(lastMsg);



                        } else {
                            holder.messageTime.setText("00:00");
                            holder.userStatus.setText("Tap to chat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.username.setText(user.getName());

        Glide.with(context).load(users.get(position).getPiMage()).placeholder(R.drawable.ic_avatar).into(holder.profileImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChartActivity.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("uid", user.getUid());
                intent.putExtra("image", user.getPiMage());





                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        de.hdodenhof.circleimageview.CircleImageView profileImg;
        TextView username,messageTime,userStatus;



        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profileImg);
            username = itemView.findViewById(R.id.username);
            messageTime = itemView.findViewById(R.id.messageTime);
            userStatus = itemView.findViewById(R.id.userStatus);
        }
    }




}

