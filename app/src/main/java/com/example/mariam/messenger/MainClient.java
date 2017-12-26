package com.example.mariam.messenger;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainClient extends AsyncTask<String, Void , Void> {
    public Socket clientSocket = null;
    private Thread updaterThread = null;
    private PrintWriter writer= null;
    private BufferedReader reader= null;
    private ResultListener lst = null;
    private DataUpdateListener ulst = null;

    public MainClient(Context context) {
        this.lst = (ResultListener)context;
        this.ulst = (DataUpdateListener)context;
    }

    public boolean connect() {
        try {
            clientSocket = new Socket(InetAddress.getByName(ConstansContainer.SERVER_URL),ConstansContainer.SOCKET_PORT);
           if (clientSocket != null) {
               return true;
           }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void disconnect()
    {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int login(String uName, String password) {
        char[] buffer = new char[1024];
        try {
            if (connect()) {
                writer = new PrintWriter(clientSocket.getOutputStream());
                writer.write("login?" + uName + ";" + password);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                reader.read(buffer);
                String buf = new String(buffer);
                return (buf.contains("YES"))?1:0;
            } else {
                return -1;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int register(String uName, String password)
    {

        char[] buffer = new char[1024];
        try {
            if (connect()) {
                writer = new PrintWriter(clientSocket.getOutputStream());
                writer.write("registration?" + uName + ";" + password);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                reader.read(buffer);
                String buf = new String(buffer);
                return (buf.contains("YES"))?1:0;
            } else {
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int logout(String userName)
    {
        char[] buffer = new char[1024];
        try {
            if (connect()) {
                writer = new PrintWriter(clientSocket.getOutputStream());
                writer.write("logout?" + userName);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                reader.read(buffer);
                return (new String(buffer).contains("YES"))?1:0;
            } else {
                return -1;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Map<String, User> getOnlines()
    {
        char[] buffer = new char[4096];
        try {
            writer = new PrintWriter(clientSocket.getOutputStream());
            writer.write("update?");
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            reader.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return setMap(new String(buffer));
    }

    public Map<String,User> setMap(String str) {
        int f1 = 0, f2;
        String ip, userName;
        Map<String,User> map = new HashMap<>();
        while(true) {
            f2 = str.indexOf(";", f1);
            if(f2 == -1) {
                break;
            }
            ip = str.substring(f1, f2);
            f2 += 1;
            f1 = str.indexOf("\n", f2);
            userName = str.substring(f2, f1);
            f1 += 1;
            map.put(userName,new User(userName,ip,null));
        }
        return map;
    }

    public void startUpdateTimer() {
        updaterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (updaterThread != null) {
                    try {
                        ulst.dataReceiver(getOnlines());
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        updaterThread.start();
    }

    public void interruptUpdateTimer() {
        if (updaterThread != null) {
            updaterThread = null;
        }
    }
    static int a = 0;
    @Override
    protected Void doInBackground(String... params) {
        switch (params[0]) {
            case "login":
                int loginResult = login(params[1], params[2]);
                lst.onResultLogin(loginResult);
                break;
            case "registration":
                a++;
                lst.onResultRegister(register(params[1], params[2]));
                break;
            case "logout":
                lst.onLogout(logout(params[1]));
                break;
        }
        return null;
    }

}
