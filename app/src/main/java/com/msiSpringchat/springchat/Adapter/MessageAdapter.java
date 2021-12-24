package com.msiSpringchat.springchat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.msiSpringchat.springchat.Holder.Message;
import com.msiSpringchat.springchat.R;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public MessageAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messages = messages;

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_item, parent, false);
            return new SendViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.recever_item, parent, false);
            return new RecevViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getClass() == SendViewHolder.class){
            SendViewHolder viewHolder = (SendViewHolder)holder;
            viewHolder.message.setText(message.getMessage());




        }else {
            RecevViewHolder viewHolder = (RecevViewHolder)holder;
            viewHolder.message.setText(message.getMessage());

        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    public class SendViewHolder extends  RecyclerView.ViewHolder{

        TextView message;
        TextView timeofmessage;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }
    }

    public class RecevViewHolder extends  RecyclerView.ViewHolder{
        TextView message;
        TextView timeofmessage;

        public RecevViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }
    }
}
