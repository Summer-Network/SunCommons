package com.vulcanth.commons.listeners.collections;

import com.vulcanth.commons.listeners.ListenersAbstract;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.role.Role;
import com.vulcanth.commons.utils.tags.TagUtils;
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

        Role.reset(player);
        event.setQuitMessage(null);
    }

}
