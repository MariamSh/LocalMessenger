package com.example.mariam.messenger;

/**
 * Created by mariam on 22/11/17.
 */
public interface ResultListener {
    void onResultLogin(int flag);
    void onResultRegister(int flag);
    void onLogout(int flag);
}
