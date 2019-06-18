package com.example.ex4;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/*
 This activity responsible of drawing the second window.
 This window shows the joystick waiting for movement.
 */
public class JoystickActivity extends AppCompatActivity implements JoystickView.JoystickListener {

    TcpClient tcpClient;


    /*
     This func get the ip and port inserted by the user and creates tcpClient
     object to connect the server.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JoystickView joystick = new JoystickView(this);
        setContentView(R.layout.activity_joystick);

        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        int port = Integer.parseInt(intent.getStringExtra("port"));

        tcpClient = new TcpClient(port, ip);
        tcpClient.connect();
    }

    /*
     This func destroy tcpClient connection when app exit the joystick screen.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        tcpClient.stopClient();
    }

    /*
     This func send the command to the server.
     */
    @Override
    public void onJoystickMoved(float aileron, float elevator, int id) {
        Log.d("Joystick", "X : " + aileron + " Y : " + elevator);


        tcpClient.sendMessage("set /controls/flight/elevator " + elevator );
        tcpClient.sendMessage("set /controls/flight/aileron " + aileron);


    }
}