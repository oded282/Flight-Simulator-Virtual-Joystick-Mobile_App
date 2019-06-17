package com.example.ex4;

import android.util.Log;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import static android.content.ContentValues.TAG;
 /*
  This class responsible of the connection to the server.
  */
public class TcpClient {

    private String ip;
    private int port;
    private Socket socket;
    private OutputStream stream;

    /*
     tcpClient constructor.
     */
    public TcpClient(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    /*
     This func responsible to connect and create socket.
     */
    public void connect(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, ip + port);
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);

                    try {
                        stream = socket.getOutputStream();

                    } catch (Exception e) {
                        Log.e("TCP", "S: Error", e);
                    }
                } catch (Exception e) {
                    Log.e("TCP", "C: Error", e);
                }
            }
        };
        Thread thread = new Thread(r);
        thread.start();
    }


    /*
     This func sends commands to the plane, using another thread.
     */
    public void sendMessage(final String message) {



        Log.d("TCP Client", "C: inside send message func");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                if (stream != null) {
                    String s = message + "\r\n";
                    byte[] buf = s.getBytes();
                    Log.d(TAG, s);
                    stream.write(buf);
                    stream.flush();
                }
                }catch (Exception e){
                    Log.e("stream", "S: Error", e);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Log.d(TAG, "message sent.");
    }

    /*
     Close the connection and release the members
     */
    public void stopClient() {
        try {
            socket.close();
        }catch (Exception e){
            Log.e("TCP", "C: Error", e);
        }finally {
            try {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }catch (Exception e){
                Log.e("stream", "S: Error", e);
            }
            stream = null;
        }
    }
}
