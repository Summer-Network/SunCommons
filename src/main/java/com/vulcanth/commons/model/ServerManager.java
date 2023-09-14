package com.vulcanth.commons.model;

import com.vulcanth.commons.Main;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class ServerManager {

    public static void setupServers() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), ()-> Arrays.stream(Server.values()).filter(server -> !server.listServers().isEmpty()).forEach(server -> server.listServers().forEach(ServerInfo::update)), 5L, 10L);
    }
}
