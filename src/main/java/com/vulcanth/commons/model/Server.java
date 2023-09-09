package com.vulcanth.commons.model;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.player.Profile;

import java.util.ArrayList;
import java.util.List;

public enum Server {

    LOBBY("47 : 1 : nome>&6Lobby Principal : desc>&eClique para conectar", 12, "ca-01.antryhost.com:25567:lobby1"),
    SKYWARS("GRASS : 1 : nome>&aSky Wars &b&lV0.0\n \n &eConfira a atualização &n0.0\n &bvulcanth.com/skywars\n \n &7Você tem medo de altura? Então este\n &7jogo não é para você! No Sky Wars, você\n \n& 7deverá eliminar os seus adversários com\n \n&7e ajuda de diversos Kits e Habilidades.\n \n &8• &fSolo\n &8• &fDupla\n \n&aClique para conectar!", 1, "ca-01.antryhost.com:25568:skywars1"),
    BEDWARS("", 1, ""),

    THE_BRIDGE("", 1, ""),
    BUILD_BATTLE("", 1, ""),
    TEST_ZONE("", 1, ""),
    SKY_BLOCK("", 1, "");

    private final String item;
    private final int slot;
    private final List<ServerInfo> servers;


    Server(String item, int slot, String... serversIP) {
        this.item = item;
        this.slot = slot;
        this.servers = new ArrayList<>();

        for (String server : serversIP) {
            String ip = server.split(":")[0];
            String serverName = server.split(":")[2];
            int port = Integer.parseInt(server.split(":")[1]);
            this.servers.add(new ServerInfo(serverName, ip, port));
        }
    }

    public String getItemString() {
        return this.item;
    }

    public int getSlot() {
        return this.slot;
    }

    public void connect(Profile profile) {
    }

    public List<ServerInfo> listServers() {
        return this.servers;
    }
}
