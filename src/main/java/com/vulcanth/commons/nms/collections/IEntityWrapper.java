package com.vulcanth.commons.nms.collections;

import com.vulcanth.commons.library.holograms.HologramLine;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface IEntityWrapper {
    int getId();
    void setPassengerOf(Entity entity);

    void setItemStack(ItemStack item);

    void setName(String name);

    boolean isDead();

    void killEntity();

    Entity getEntity();

    HologramLine getLine();

    void setLocation(Location location);
}
