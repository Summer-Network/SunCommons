package com.vulcanth.commons.bungee.listeners.collections;

import com.vulcanth.commons.bungee.listeners.ListenersAbstract;
import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.event.EventHandler;

public class ProxiedQuitEvents extends ListenersAbstract {

    @EventHandler
    public void ProxiedQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ProxiedProfile profile = ProxiedProfile.loadProfile(player.getName());
        if (profile != null) {
            profile.destroy();
        }
    }
}
