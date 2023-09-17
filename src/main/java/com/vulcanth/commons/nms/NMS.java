package com.vulcanth.commons.nms;

import com.vulcanth.commons.library.holograms.HologramLine;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMS {
    void sendAction(Player player, String message);
    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
    void sendCustomTab(Player player, String header, String footer);
    void setValueAndSignature(Player player, String value, String signature);
}
