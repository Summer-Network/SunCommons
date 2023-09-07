package com.vulcanth.commons.model;

import com.vulcanth.commons.Main;

import java.util.List;

public enum Server {

    LOBBY("47 : 1 : nome>&6Lobby Principal : desc>&eClique para conectar", 12, ""),
    SKYWARS("GRASS : 1 : nome>&aSky Wars &b&lV0.0\n \n &eConfira a atualização &n0.0\n &bvulcanth.com/skywars\n \n &7Você tem medo de altura? Então este\n &7jogo não é para você! No Sky Wars, você\n \n& 7deverá eliminar os seus adversários com\n \n&7e ajuda de diversos Kits e Habilidades.\n \n &8• &fSolo\n &8• &fDupla\n \n&aClique para conectar!", 1, ""),
    BEDWARS("", 1, ""),

    THE_BRIDGE("", 1, ""),
    BUILD_BATTLE("", 1, ""),
    TEST_ZONE("", 1, ""),
    SKY_BLOCK("", 1, "");

    private String item;
    private int slot;
    private List<String> servers;

    Server(String item, int slot, String serverLink) {
        this.item = item;
        this.slot = slot;
        this.servers = Main.getInstance().getVulcanthConfig().findYamlByFileLink("config.yml").getYamlConfig().getStringList("servers_ip." + serverLink);
    }
}
