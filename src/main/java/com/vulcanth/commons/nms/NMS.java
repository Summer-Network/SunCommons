package com.vulcanth.commons.nms;

import com.vulcanth.commons.library.holograms.HologramLine;
import com.vulcanth.commons.nms.collections.IEntityWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMS {
    void sendAction(Player player, String message);
    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
    void sendCustomTab(Player player, String header, String footer);
    void setValueAndSignature(Player player, String value, String signature);
    IEntityWrapper createArmorStand(Location location, HologramLine line, ItemStack item);
    IEntityWrapper createItem(Location location, HologramLine line, ItemStack item);
    IEntityWrapper createSlime(Location location, HologramLine line);
}
