package com.example.chatbsendiri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class ChatBoxActivity extends AppCompatActivity {

        public RecyclerView myRecylerView ;
        public List<Message> MessageList ;
        public ChatBoxAdapter chatBoxAdapter;
        public EditText messagetxt ;
        public Button send ;
        public String Nama;
        private io.socket.client.Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://34.87.57.218:8765");
            mSocket.connect();
            Log.d("loglog", "instance initializer: "+mSocket.connected());
        } catch (URISyntaxException e) {
            Log.d("myTag", e.getMessage());
        }

    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat_box);

            messagetxt = findViewById(R.id.message) ;
            send = findViewById(R.id.send);

            Nama = getIntent().getStringExtra("NAMA");
            Log.d("logAfter", "onCreate: "+Nama);

            MessageList = new ArrayList<>();
            myRecylerView = findViewById(R.id.messagelist);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            myRecylerView.setLayoutManager(mLayoutManager);
            myRecylerView.setItemAnimator(new DefaultItemAnimator());

            setListening();

            send.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String message = messagetxt.getText().toString().trim();
                    messagetxt.setText("");
                    if (!message.isEmpty()) {


                        try {
                            JSONObject jsonData = new JSONObject();
                            jsonData.put("nickname",Nama);
                            jsonData.put("message",message);
                            mSocket.emit("message", jsonData);
                        } catch (JSONException e) {
                            Log.d("me", "error send message " + e.getMessage());
                        }
                    }
                }
            });


        }


    private void setListening() {
        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                if (args[0].toString().contains("{")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = (JSONObject) args[0];
                            try {

                                String nickname = data.getString("nickname");
                                String message =  data.getString("message");
//                                String message = args[0].toString();

                                Message m = new Message(nickname,message);

                                MessageList.add(m);
                                chatBoxAdapter = new ChatBoxAdapter(MessageList,Nama);

                                chatBoxAdapter.notifyDataSetChanged();
                                myRecylerView.setAdapter(chatBoxAdapter);
                                myRecylerView.scrollToPosition(chatBoxAdapter.getItemCount()-1);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
//                try {
                    Log.d("loglog", "call: "+args[0].toString());
//                    JSONObject messageJson = new JSONObject(args[0].toString());
//                    message = messageJson.getString("message");

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.d("loglog", "call: "+e.getMessage());
//                }
            }
        });
    }
    }