package com.vulcanth.commons.nms.npcs;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface INPCEntity {

    void setName(String text);
    void kill();
    void setLocation(double x, double y, double z, World world, float yam, float pitch);
    void spawn();
    void setItemInHand(ItemStack item);
    void sendPackets(Player... player);
    void setShowNick(boolean showNick);
    Player getPlayer();
    void update();
    void update(Player player);
    List<Player> packetsPlayer();

}
