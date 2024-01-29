package com.summer.commons.library.hologram;

import com.summer.commons.nms.NmsManager;
import com.summer.commons.nms.hologram.IHologramEntity;
import org.bukkit.Location;

public class HologramLine {

    private int index;
    private IHologramEntity entity;
    private String text;
    private Location location;

    public HologramLine(String text, int index, Location location) {
        this.text = text;
        this.index = index;
        this.location = location;
        this.entity = NmsManager.spawnHologram(this.location);
        this.entity.setName(this.text);
    }

    public void sendPacket() {
        //this.entity.sendPackets(this.location);
    }

    public int getIndex() {
        return this.index;
    }

    public String getText() {
        return this.text;
    }

    public void updateText(String text) {
        this.entity.setName(text);
    }

    public void destroy() {
        this.entity.kill();
        this.text = null;
        this.index = 0;
        this.location = null;
        this.entity = null;
    }
}
