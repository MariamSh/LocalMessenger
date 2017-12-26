package com.example.mariam.messenger;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class User {

    private String username = null;
    private String ip = null;
    public boolean hasNewMessage = false;
    public User (String username,String ip, Socket uSocket) {
        this.username = username;
        this.ip = ip;
    }

    public String getUsername() {
        return this.username;
    }

    public InetAddress getIp() throws UnknownHostException {
        return InetAddress.getByName(ntoa(Long.parseLong(ip)));
    }

    public String getStringIp() {
        return ntoa(Long.parseLong(ip));
    }

    private String ntoa(long raw) {
        byte[] b = new byte[] {(byte)raw,  (byte)(raw >> 8),(byte)(raw >> 16), (byte)(raw >> 24)};
        try {
            return InetAddress.getByAddress(b).getHostAddress();
        } catch (UnknownHostException e) {
            //No way here
            return null;
        }
    }
}
