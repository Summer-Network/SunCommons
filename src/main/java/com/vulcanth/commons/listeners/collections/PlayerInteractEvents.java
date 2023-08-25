package com.vulcanth.commons.listeners.collections;

import com.vulcanth.commons.listeners.ListenersAbstract;
import com.vulcanth.commons.nms.NMS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractEvents extends ListenersAbstract {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
    }

}
