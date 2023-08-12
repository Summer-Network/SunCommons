package com.vulcanth.commons.listeners.collections;

import com.vulcanth.commons.listeners.ListenersAbstract;
import com.vulcanth.commons.player.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEvents extends ListenersAbstract {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.loadProfile(player.getName());
        if (profile != null) {
            profile.destroy(true);
        }

        event.setQuitMessage(null);
    }

}
