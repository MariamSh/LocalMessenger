package com.example.mariam.messenger;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class MessagingServer extends AsyncTask <Void,Void,Void>{
    ServerSocket serverSocket = null;
    private BufferedReader reader = null;
    char []buf;
    private ContainerActivity activity = null;

    public MessagingServer(Activity activity) {
        this.activity = (ContainerActivity) activity;

    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(ConstansContainer.SOCKET_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void receiveMessage(String message, String ip) {
        String userName = activity.getUserNameByIp(ip);
        activity.getMessagesManager().openDB();
        MessageModel m = activity.getMessagesManager().addNewMessage(message,userName,0);
        activity.getMessagesManager().closeDb();
        if ( m != null) {
            if (activity.getCurrentFragment() instanceof ChatFragment) {

                ((ChatFragment)activity.getCurrentFragment()).updateAdapter(m);
            } else {
                ((UsersListFragment)activity.getCurrentFragment()).changeColor(m.getPair());
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        startServer();
        while (serverSocket !=null) {
            try {
                final Socket socket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            buf = new char[1024];
                            while (true) {
                                buf[0] = '\u0000';
                                reader.read(buf);
                                if (buf[0] != '\u0000' && activity.isLoginedIn()){
                                    receiveMessage(String.valueOf(buf), socket.getInetAddress().toString());
                                } else {
                                    if(socket != null) {
                                        socket.close();
                                    }
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("message", "ENDDDDD");
        }
        return null;
    }


}
