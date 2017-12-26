package com.example.mariam.messenger;

import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by mariam on 12/11/17.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{
    private Button loginButton = null;
    private Button regButton = null;
    private EditText username = null;
    private EditText password = null;
    private ContainerActivity activity = null;
    public EditText confirmPasswordEditText = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,null,false);
        loginButton = (Button)view.findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);
        regButton = (Button)view.findViewById(R.id.btn_register);
        regButton.setOnClickListener(this);
        username = (EditText)view.findViewById(R.id.et_username);
        password = (EditText)view.findViewById(R.id.et_password);
        confirmPasswordEditText = (EditText) view.findViewById(R.id.confirm_password);
        activity = (ContainerActivity)getActivity();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                try {
                    runMainClient("login");
                } catch (IllegalStateException e) {
                    activity.mainClient =  new MainClient(activity);
                    runMainClient("login");
                }
                break;
            case R.id.btn_register:
                if (confirmPasswordEditText.getVisibility() == View.GONE) {
                   confirmPasswordEditText.setVisibility(View.VISIBLE);
                } else {
                    if (password.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                        try {
                            runMainClient("registration");
                        } catch (IllegalStateException e) {
                            activity.mainClient =  new MainClient(activity);
                            runMainClient("registration");
                        }
                    } else {
                        activity.invalidCommand("Your passwords do not match");
                    }
                }
                break;
        }
    }

    private void runMainClient(String command) {
        String userName = username.getText().toString();
        String passwd = password.getText().toString();
        if (userName.isEmpty() || passwd.isEmpty()) {
            activity.invalidCommand("Login/Password field(s) is empty");
        } else {
            activity.mainClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    command, userName, passwd);
        }
    }

    public String getCurrentUserName() {
        return username.getText().toString();
    }

}
