package com.vulcanth.commons.library.npc.listeners;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.library.NPCManager;
import com.vulcanth.commons.library.npc.NPC;
import com.vulcanth.commons.listeners.ListenersAbstract;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class NPCListeners extends ListenersAbstract {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), ()-> {
           if (!NPCManager.listNPC().isEmpty()) {
               if (!event.getFrom().getChunk().equals(event.getTo().getChunk())) {
                   for (NPC npc : NPCManager.listNPC(true, player)) {
                       if (npc.getLocation().getChunk().equals(event.getTo().getChunk())) {
                           if (player.canSee(npc.getEntity().getPlayer())) {
                               Bukkit.getScheduler().runTask(Main.getInstance(), () -> npc.getEntity().update(player));
                           }
                       }
                   }
               }
           }
        });
    }

}
