package com.example.chatbsendiri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class CHATchat extends AppCompatActivity {

    public Button button;
    public TextView text;
    public EditText edit;
    public String message;
    private io.socket.client.Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://10.0.112.194:8765");
            mSocket.connect();
            Log.d("loglog", "instance initializer: "+mSocket.connected());
        } catch (URISyntaxException e) {
            Log.d("myTag", e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatchat);
        button = (Button) findViewById(R.id.button);
        text =  (TextView) findViewById(R.id.textView);
        edit =  (EditText) findViewById(R.id.editText);
        mSocket.connect();
        text.setText("");
        setListening();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String message = edit.getText().toString().trim();
                edit.setText("");
                if (!message.isEmpty()) {
                    //send message
                    String jsonString = "{message: " + "'" + message + "'" + "}";

                    try {
                        JSONObject jsonData = new JSONObject(jsonString);
                        mSocket.emit("message", jsonData);
                    } catch (JSONException e) {
                        Log.d("me", "error send message " + e.getMessage());
                    }
                }
            }
        });

    }//on create
    private void setListening() {
        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    Log.d("loglog", "call: "+args[0].toString());
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    message = messageJson.getString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(message);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("loglog", "call: "+e.getMessage());
                }
            }
        });
    }

}//end of class