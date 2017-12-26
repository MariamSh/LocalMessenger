package com.example.mariam.messenger;

import java.util.Map;

public interface DataUpdateListener {
    void dataReceiver(Map<String, User> map);
}
