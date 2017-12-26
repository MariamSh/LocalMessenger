package com.example.mariam.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class UsersListFragment extends Fragment implements ListView.OnItemClickListener {
    private ContainerActivity activity = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);
        activity = (ContainerActivity)getActivity();
        ListView usersList = (ListView)view.findViewById(R.id.lw_user_list);
        usersList.setAdapter(activity.adapter);
        usersList.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.mainClient.startUpdateTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        activity.mainClient.interruptUpdateTimer();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String uName = activity.adapter.getUserName(position);
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setUser(activity.getOnlineUsersMap().get(uName));
        ((ContainerActivity) getActivity()).fragmentTransaction(chatFragment);
    }

    public void changeColor(final String pairName) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.adapter.changeColor(pairName);
            }
        });
    }
}
