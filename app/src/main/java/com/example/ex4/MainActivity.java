package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


/*
 This activity responsible of drawing the main app window.
 The window got two text box, the user can set ip and port
 to connect the server.
 */
public class MainActivity extends AppCompatActivity {


    /*
     This func creates the first screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
     This func is activated following connect button being pressed.
     Change to joystick window and send the ip and port to the next window.
     */
    public void ConnectButton(View view){

        EditText ipText = (EditText) findViewById(R.id.ip);
        EditText portText = (EditText) findViewById(R.id.port);
        String ip = ipText.getText().toString();
        String port = portText.getText().toString();

        Intent intent = new Intent(this, JoystickActivity.class);

        intent.putExtra("ip" , ip);
        intent.putExtra("port" , port);

        startActivity(intent);
    }
}
