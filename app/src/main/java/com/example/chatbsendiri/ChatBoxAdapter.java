package com.example.chatbsendiri;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatBoxAdapter  extends RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder> {
    private List<Message> MessageList;
    private String nickname;

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView message;
        public LinearLayout llChat;


        public MyViewHolder(View view) {
            super(view);

            nickname = (TextView) view.findViewById(R.id.nickname);
            message = (TextView) view.findViewById(R.id.message);
            llChat = (LinearLayout) view.findViewById(R.id.llChat);





        }
    }
// in this adaper constructor we add the list of messages as a parameter so that
// we will passe  it when making an instance of the adapter object in our activity



    public ChatBoxAdapter(List<Message>MessagesList, String nickname) {

        this.MessageList = MessagesList;
        this.nickname = nickname;


    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }
    @Override
    public ChatBoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);



        return new ChatBoxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatBoxAdapter.MyViewHolder holder, final int position) {

        //binding the data from our ArrayList of object to the item.xml using the viewholder
        Message m = MessageList.get(position);
        holder.nickname.setText(m.getNickname());
        holder.message.setText(m.getMessage() );

        if (m.getNickname().equals(nickname)){
            holder.llChat.setGravity(Gravity.END);
            holder.message.setBackgroundResource(R.drawable.round2);
            holder.nickname.setTextColor(Color.parseColor("#579296"));

        }else {
            holder.llChat.setGravity(Gravity.START);
            holder.message.setBackgroundResource(R.drawable.round1);
            holder.nickname.setTextColor(Color.parseColor("#4a7dcf"));
        }




    }



}
