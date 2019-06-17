package com.example.ex4;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class JoystickActivity extends AppCompatActivity implements JoystickView.JoystickListener {

    TcpClient tcpClient;

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

    @Override
    public void onJoystickMoved(float aileron, float elevator, int id) {
        Log.d("Joystick", "X : " + aileron + " Y : " + elevator);


        tcpClient.sendMessage("set /controls/flight/elevator " + elevator );
        tcpClient.sendMessage("set /controls/flight/aileron " + aileron);


    }
}