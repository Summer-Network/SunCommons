package com.vulcanth.commons.nms;

import org.bukkit.entity.Player;

public interface NMS {
    void sendAction(Player player, String message);
    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
    void sendCustonTab(Player player, String header, String footer);

}
