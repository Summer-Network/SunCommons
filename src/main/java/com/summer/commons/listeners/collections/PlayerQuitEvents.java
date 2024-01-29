package com.summer.commons.listeners.collections;

import com.summer.commons.listeners.ListenersAbstract;
import com.summer.commons.model.SkinCacheCommand;
import com.summer.commons.player.Profile;
import com.summer.commons.player.role.Role;
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

        SkinCacheCommand.removeSkinProgress(player.getName());
        Role.reset(player);
        event.setQuitMessage(null);
    }

}
