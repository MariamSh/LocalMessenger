package com.example.mariam.messenger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MessagesManager {

    private DataBaseHelper dbHelper = null;
    private SQLiteDatabase db = null;

    public MessagesManager(Context context,String uName) {
        dbHelper = new DataBaseHelper(context,uName);
    }

    public void openDB() {
        this.db = dbHelper.openDB();
    }

    public ArrayList<MessageModel> readAllMessages(String pairName)
    {
        MessageModel []a = dbHelper.readAllData(pairName);
        ArrayList<MessageModel> list = new ArrayList<>();
        for (int i = 0; i < a.length; ++i) {
            list.add(a[i]);
        }
        return list;
    }


    public synchronized MessageModel addNewMessage(String msg, String userName,int sendByMe) {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        MessageModel m = new MessageModel(userName,msg,date.toString(),sendByMe);
        dbHelper.insertData(m,db);
        return m;
    }

    public void closeDb() {
        dbHelper.closeDb();
    }

}
