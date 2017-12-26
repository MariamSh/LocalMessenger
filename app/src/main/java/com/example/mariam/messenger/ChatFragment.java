package com.example.mariam.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener {
    private TextView userName = null;
    private EditText editText = null;
    private ImageButton sendButton = null;
    private Client client = null;
    private User user = null;
    private ContainerActivity activity = null;
    private ListView messagesListView = null;
    MessagesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        messagesListView = (ListView) view.findViewById(R.id.messages_list);
        userName = (TextView)view.findViewById(R.id.tv_username);
        editText = (EditText) view.findViewById(R.id.editText);
        sendButton = (ImageButton) view.findViewById(R.id.btn_send);
        sendButton.setOnClickListener(this);
        userName.setText(user.getUsername());
        activity =(ContainerActivity) getActivity();
        adapter = new MessagesAdapter(activity, (activity.getMessagesManager()).readAllMessages(user.getUsername()));
        messagesListView.setAdapter(adapter);
        client = new Client(user, activity);
        client.execute();
        editText.setOnFocusChangeListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        String m= editText.getText().toString().trim();
        if (!m.isEmpty()) {
            client.sendMessage(m);
            editText.setText("");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disConnect();
    }

    public void updateAdapter(final MessageModel m) {
        adapter.addNewMessage(m);
        if (m.getPair().equals(user.getUsername())) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,"New Message From "+m.getPair(),1).show();
                    activity.newMessage(m.getPair());
                }
            });

        }
    }

    public void setUser (User user) {
        this.user = user;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId() == R.id.editText && hasFocus) {
            sendButton.setVisibility(View.VISIBLE);
        }
    }
}
