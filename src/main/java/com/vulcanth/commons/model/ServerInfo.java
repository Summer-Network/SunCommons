package com.vulcanth.commons.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerInfo {

    private final String serverName;
    private final String ip;
    private final Integer port;
    private int online;
    private int maxPlayers;
    private String moted;

    public ServerInfo(String serverName, String ip, Integer port) {
        this.serverName = serverName;
        this.ip = ip;
        this.port = port;
        this.online = 0;
        this.maxPlayers = 0;
        this.moted = "";
    }

    public String getServerName() {
        return this.serverName;
    }

    public int getOnline() {
        return this.online;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public String getMoted() {
        return this.moted;
    }

    public void update() {
        try {
            Socket sock = new Socket();

            sock.connect(new InetSocketAddress(this.ip, this.port), 10);

            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());
            out.write(0xFE);

            StringBuffer str = new StringBuffer();

            int b;
            while ((b = in.read()) != -1) {
                if (b > 16 && b != 255 && b != 23 && b != 24) {
                    str.append((char) b);
                }
            }

            String[] data = str.toString().split("§");
            this.online = Integer.parseInt(data[1]);
            this.maxPlayers = Integer.parseInt(data[2]);
            this.moted = data[0].substring(2);
        } catch (Exception ex) {
            this.online = 0;
            this.maxPlayers = 0;
            this.moted = "";
        }
    }
}
