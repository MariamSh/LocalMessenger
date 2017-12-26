package com.example.mariam.messenger;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends AsyncTask<String, Void, Void> {
    private Socket pairSocket = null;
    private PrintWriter writer = null;
    private User user = null;
    private ContainerActivity activity = null;

    public Client(User user, ContainerActivity activity) {
        this.user = user;
        this.activity = activity;
    }

    public void sendMessage(String message) {
       if(writer != null && pairSocket != null) {
           writer.write(message);
           writer.flush();
           activity.getMessagesManager().openDB();
           MessageModel m = activity.getMessagesManager().addNewMessage(message, user.getUsername(), 1);
           ((ChatFragment) activity.getCurrentFragment()).updateAdapter(m);
           activity.getMessagesManager().closeDb();
       } else {
           Toast.makeText(activity, user.getUsername() + " is offline", Toast.LENGTH_SHORT).show();
       }
    }

    public void disConnect() {
        try {
            if(pairSocket != null) {
                pairSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            pairSocket = new Socket(user.getIp(),ConstansContainer.SOCKET_PORT);
            writer = new PrintWriter(pairSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
