package com.summer.commons.bungee.listeners.collections;

import com.summer.commons.bungee.BungeeMain;
import com.summer.commons.bungee.listeners.ListenersAbstract;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;

public class ServerEvents extends ListenersAbstract {

    @EventHandler @Deprecated
    public void onServerPing(ProxyPingEvent event) {
        ServerPing serverPing = event.getResponse();
        if (BungeeMain.isIsMaintence()) {
            serverPing.setVersion(new ServerPing.Protocol("MANUTENÇÃO", 0));
        } else {
            serverPing.setPlayers(serverPing.getPlayers());
        }

        serverPing.setFavicon(event.getResponse().getFavicon());
        serverPing.setDescription("§b§lSummer§b.gg §7[1.8 - 1.20]\n§eNovidades de lançamento!");
        event.setResponse(serverPing);
    }
}
