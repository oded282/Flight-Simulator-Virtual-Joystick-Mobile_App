package com.example.ex4;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import static android.content.ContentValues.TAG;

public class TcpClient {

    private String ip;
    private int port;
    private Socket socket;
    private PrintWriter mBufferOut;


    public TcpClient(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    public void connect(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, ip + port);
                    InetAddress serverAddr = InetAddress.getByName("10.0.0.2");
                    Log.d("TCP Client", serverAddr.toString());
                    Log.d("TCP Client", "C: Connecting...");
                    socket = new Socket(serverAddr, 5402);

                    try {
                        Log.d("TCP Client", "create the buffer");
                        mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                socket.getOutputStream())), true);

                    } catch (Exception e) {
                        Log.e("TCP", "S: Error", e);
                    }
                } catch (Exception e) {
                    Log.e("TCP", "C: Error", e);
                }
                Log.d("TCP Client", "C: Connected succeeded");
            }
        };
        Thread thread = new Thread(r);
        thread.start();

    }


    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(final String message) {
        Log.d("TCP Client", "C: inside send message func");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    Log.d(TAG, message + " /r/n");
                    mBufferOut.write(message + " /r/n");
                    mBufferOut.flush();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Log.d(TAG, "message sent.");
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {
        try {
            socket.close();
        }catch (Exception e){
            Log.e("TCP", "C: Error", e);
        }finally {
            if (mBufferOut != null) {
                mBufferOut.flush();
                mBufferOut.close();
            }
            mBufferOut = null;
        }
    }
}
