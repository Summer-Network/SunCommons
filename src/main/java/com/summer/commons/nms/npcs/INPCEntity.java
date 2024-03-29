package com.summer.commons.nms.npcs;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface INPCEntity {

    void setName(String text);
    void kill();
    void setLocation(World world, double x, double y, double z);
    void spawn();
    void setItemInHand(ItemStack item);
    void sendPackets(Player... player);
    void setShowNick(boolean showNick);
    Player getPlayer();
    void update();
    void update(Player player);
    List<Player> packetsPlayer();

}
