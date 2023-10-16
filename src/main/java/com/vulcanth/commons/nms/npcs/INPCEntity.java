package com.vulcanth.commons.nms.npcs;

import org.bukkit.Location;

public interface INPCEntity {

    void setName(String text);
    void kill();
    void sendPackets(Location location);

    void setLocation(double x, double y, double z);
}
