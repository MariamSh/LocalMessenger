package com.example.mariam.messenger;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mariam on 20/11/17.
 */

public class UsersListAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater = null;
    private ArrayList<User> onlineUsers;

    public UsersListAdapter(Context context) {
        super(context, R.layout.users_item);
        onlineUsers = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.users_item,null,false);
        User currentUser = onlineUsers.get(position);
        ((TextView) view.findViewById(R.id.online_user_name)).setText(currentUser.getUsername());
        if (currentUser.hasNewMessage) {
            view.setBackgroundColor(view.getResources().getColor(android.R.color.holo_red_dark));
        }
        return view;
    }

    @Override
    public void addAll(Collection<? extends String> collection) {
        super.addAll(collection);
        ArrayList<String> userNames = (ArrayList<String>)collection;
        ArrayList<User> newUsers = new ArrayList<>();
        for (String userName: userNames) {
            User user = getUserFromList(userName);
            if (user == null) {
                user = new User(userName, "", null);
            }
            newUsers.add(user);
        }
        onlineUsers.clear();
        onlineUsers.addAll(newUsers);
    }

    private User getUserFromList(String userName) {
        for (User currentUser: onlineUsers) {
            if (currentUser.getUsername().equals(userName)) {
                return currentUser;
            }
        }
        return null;
    }

    public void changeColor(String userName) {
        for (User currentUser: onlineUsers) {
            if (currentUser.getUsername().equals(userName)) {
                currentUser.hasNewMessage = true;
                notifyDataSetChanged();
                break;
            }
        }
    }

    public String getUserName(int posintion) {
        if (onlineUsers == null) {
            return null;
        }
        onlineUsers.get(posintion).hasNewMessage = false;
        return onlineUsers.get(posintion).getUsername();
    }
}
