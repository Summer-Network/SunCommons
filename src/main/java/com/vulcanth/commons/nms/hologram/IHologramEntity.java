package com.vulcanth.commons.nms.hologram;

import org.bukkit.Location;

public interface IHologramEntity {

    void setName(String text);
    void kill();
    void sendPackets(Location location);

    void setLocation(double x, double y, double z);
}
