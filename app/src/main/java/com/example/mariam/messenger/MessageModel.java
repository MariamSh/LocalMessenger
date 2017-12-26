package com.example.mariam.messenger;

import java.io.Serializable;

public class MessageModel implements Serializable {

    private String time = null;
    private String message = null;
    private String pair = null;
    private int sentByMe = -1;

    public MessageModel(String pair, String message, String time, int sentByMe) {
        this.pair = pair;
        this.message = message;
        this.time = time;
        this.sentByMe = sentByMe;
    }

    public String getTime() {
        return this.time;
    }

    public String getMessage() {
        return this.message;
    }

    public String getPair() {
        return this.pair;
    }

    public int getSentByMe() {
        return this.sentByMe;
    }
}
