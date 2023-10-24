package com.vulcanth.commons.nms;

import com.vulcanth.commons.nms.hologram.IHologramEntity;
import com.vulcanth.commons.nms.npcs.INPCEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface NMS {
    void sendAction(Player player, String message);
    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
    void sendCustomTab(Player player, String header, String footer);
    void setValueAndSignature(Player player, String value, String signature);
    IHologramEntity spawnHologramEntity(Location location);
    INPCEntity spawnNPCEntity(Location location, String npcName);
    INPCEntity spawnNPCEntity(Location location, String npcName, String value, String signature);
    void refreshPlayer(Player player);
    void look(org.bukkit.entity.Entity entity, float yaw, float pitch);
    void setHeadYaw(org.bukkit.entity.Entity entity, float yaw);
}
