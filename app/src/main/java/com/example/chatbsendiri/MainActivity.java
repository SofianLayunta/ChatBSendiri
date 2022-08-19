package com.example.chatbsendiri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private EditText nickname;
    private EditText room;
    public static final String NICKNAME = "usernickname";
//    private io.socket.client.Socket mSocket;
//    {
//        try {
//            mSocket = IO.socket("http://10.0.112.194:3001");
//            mSocket.connect();
//            Log.d("loglog2", "instance initializer: "+mSocket.connected());
//        } catch (URISyntaxException e) {
//            Log.d("myTag", e.getMessage());
//        }
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //call UI components  by id
        btn = (Button)findViewById(R.id.enterchat) ;
        room = (EditText) findViewById(R.id.room);
        nickname = (EditText) findViewById(R.id.nickname);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the nickname is not empty go to chatbox activity and add the nickname to the intent extra

                String snickname = nickname.getText().toString();
                String sroom = room.getText().toString();

                if(!nickname.getText().toString().isEmpty() && !room.getText().toString().isEmpty()){

//                        try {
//                            JSONObject jsonData = new JSONObject();
//                            jsonData.put("username",snickname);
//                            jsonData.put("room",sroom);
//                            mSocket.emit("join_room", jsonData);

                            Intent intent = new Intent(MainActivity.this, ChatBoxActivity.class);
                            intent.putExtra("nama",snickname);
                            intent.putExtra("room",sroom);
                            startActivity(intent);

//                        } catch (JSONException e) {
//                            Log.d("me", "error send message " + e.getMessage());
//                        }
                    }
                }

        });

    }



}