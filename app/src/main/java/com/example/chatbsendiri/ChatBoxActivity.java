package com.example.chatbsendiri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class ChatBoxActivity extends AppCompatActivity {

        public RecyclerView myRecylerView ;
        JSONObject listeningObject = new JSONObject();
        public List<chatting> chattingList ;
        public ChatBoxAdapter chatBoxAdapter;
        public EditText messagetxt ;
        public Button send ;
        public String Nama;
        public String Room;
        private io.socket.client.Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://34.87.57.218:8765");
//            mSocket = IO.socket("http://10.0.112.194:3001");
            mSocket.connect();
            Log.d("SocketioStatus", "instance initializer: "+mSocket.connected());
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

            Nama = getIntent().getStringExtra("nama");
            Room = getIntent().getStringExtra("room");

            myRecylerView = findViewById(R.id.messagelist);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            myRecylerView.setLayoutManager(mLayoutManager);
            myRecylerView.setItemAnimator(new DefaultItemAnimator());

            socketConnect();
            socketJoinRoom();
            socketListening();

            send.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String message = messagetxt.getText().toString().trim();
                    if (!message.isEmpty()) {



                        try {
                            JSONObject jsonData = new JSONObject();
                            jsonData.put("pesan",message);
                            jsonData.put("ruang",Room);
                            jsonData.put("userid",Nama);
                            mSocket.emit("pesan", jsonData);
                            Log.d("logSend", "onClick: "+jsonData);
                            messagetxt.setText("");

                        } catch (JSONException e) {
                            Log.d("me", "error send message " + e.getMessage());
                        }
                    }
                }
            });

//            RetriveMessage();
//            setListening();

//            send.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//
//                    String message = messagetxt.getText().toString().trim();
//                    messagetxt.setText("");
//                    if (!message.isEmpty()) {
//
//
//
//                        try {
//                            JSONObject jsonData = new JSONObject();
//                            jsonData.put("origin",Nama);
//                            jsonData.put("message",message);
//                            jsonData.put("room",Room);
//                            mSocket.emit("send_message", jsonData);
//                            Log.d("logSend", "onClick: "+jsonData);
//
//                        } catch (JSONException e) {
//                            Log.d("me", "error send message " + e.getMessage());
//                        }
//                    }
//                }
//            });


        }

        private void socketConnect(){
//            mSocket.emit("connect");
            mSocket.on("conn_status", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
//                    String data = (String) args[0];
//                    Log.d("socketIOAConnEmit", "call: "+data);
//                    try {
//                        String status = data.getString("status");
//                        Log.d("socketIOAConnEmit", "call:masuk "+status);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }

        private void socketJoinRoom(){
            List<Message> MessageList = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userid",Nama);
                jsonObject.put("ruang",Room);
                mSocket.emit("join",jsonObject);
                Log.d("socketIOAJoinEmit", "Masuk: "+jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }

            mSocket.on("join_status", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    Log.d("logDONG1", "call: MASOKMASOK"+ args[0]);

                    ChatBoxActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = (JSONObject) args[0];

                            try {
                                String status = data.getString("status");
                                String room = data.getString("join");

                                Log.d("socketIOAJoin", "Masuk: "+status+" || "+room);

                            } catch (JSONException e) {
                                return;
                            }
                        }
                    });
                }


            });

        }

        private void socketListening(){
            List<Message> MessageList = new ArrayList<>();

            mSocket.on("fback", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    ChatBoxActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray listeningArray = new JSONArray();
                            JSONObject listeningObject = new JSONObject();
                            JSONObject data = (JSONObject) args[0];

                            try {
                                String mesg = data.getString("pesan");
                                String room = data.getString("ruang");
                                String user = data.getString("userid");

                                listeningObject.put("pesan1",mesg);
                                listeningObject.put("ruang1",room);
                                listeningObject.put("userid1",user);
                                listeningArray.put(listeningObject);

                                Log.d("socketIOAListen", "Masuk: "+mesg+" || "+room+" || "+user);
                                Log.d("socketIOAListenJsonAry", "run: "+ listeningArray.length() +" || ");


                                try {
                                    for(int i=0; i < listeningArray.length(); i++){
                                        JSONObject result = listeningArray.getJSONObject(i);
                                        Message L = new Message();
                                        L.setMessage(result.getString("pesan1"));
                                        L.setRoom(result.getString("ruang1"));
                                        L.setNickname(result.getString("userid1"));
                                        Log.d("socketIOAListenJsonIII", "run: "+i);

                                        MessageList.add(L);

                                    }
                                    chatBoxAdapter = new ChatBoxAdapter(MessageList);
//
                                    chatBoxAdapter.notifyDataSetChanged();
                                    myRecylerView.setAdapter(chatBoxAdapter);
                                    myRecylerView.scrollToPosition(chatBoxAdapter.getItemCount()-1);
//
                                } catch (JSONException e) {
                                    return;
                                }

                            } catch (JSONException e) {
                                return;
                            }
                        }
                    });

                }
            });

        }

    private void RetriveMessage() {
        List<Message> MessageList = new ArrayList<>();
        try {
            JSONObject jsonData = new JSONObject();
            jsonData.put("username","ayam");
            jsonData.put("room","21");
            mSocket.emit("join_room", jsonData);
        } catch (JSONException e) {
            Log.d("me", "error send message " + e.getMessage());
        }

        mSocket.on("room_joined", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.d("logDONG1", "call: MASOKMASOK"+ args[0]);

                ChatBoxActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];

                        try {
                            JSONArray arrData = data.getJSONArray("history");
                            for(int i=0; i < arrData.length(); i++){
                                JSONObject result = arrData.getJSONObject(i);
                                Message L = new Message();
                                L.setNickname(result.getString("origin"));
                                L.setMessage(result.getString("message"));
                                L.setRoom(result.getString("room"));

                                MessageList.add(L);

                            }
                            chatBoxAdapter = new ChatBoxAdapter(MessageList);
//
                            chatBoxAdapter.notifyDataSetChanged();
                            myRecylerView.setAdapter(chatBoxAdapter);
                            myRecylerView.scrollToPosition(chatBoxAdapter.getItemCount()-1);
//
                        } catch (JSONException e) {
                            return;
                        }


                    }
                });
            }


        });


    }

    private void setListening() {

        mSocket.on("received_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("logricip", "call: "+args[0]);
                runOnUiThread(() -> {

                    List<Message> MessageList = new ArrayList<>();
                    JSONObject data = (JSONObject) args[0];

                    try {
                        JSONArray arrData = data.getJSONArray("chatHistory");
                        for(int i=0; i < arrData.length(); i++){
                            JSONObject result = arrData.getJSONObject(i);
                            Message L = new Message();
                            L.setNickname(result.getString("origin"));
                            L.setMessage(result.getString("message"));
                            L.setRoom(result.getString("room"));

                            MessageList.add(L);

                        }
                        chatBoxAdapter = new ChatBoxAdapter(MessageList);
//
                        chatBoxAdapter.notifyDataSetChanged();
                        myRecylerView.setAdapter(chatBoxAdapter);
                        myRecylerView.scrollToPosition(chatBoxAdapter.getItemCount()-1);
//


                    } catch (JSONException e) {
                        return;
                    }
                });


            }
        });

    }
    }
