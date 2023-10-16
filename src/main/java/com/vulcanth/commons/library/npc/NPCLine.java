package com.vulcanth.commons.library.npc;

import com.vulcanth.commons.nms.NmsManager;
import com.vulcanth.commons.nms.npcs.INPCEntity;
import org.bukkit.Location;

public class NPCLine {

    private int index;
    private INPCEntity entity;
    private String name;
    private Location location;

    public NPCLine(String text, int index, Location location) {
        this.name = text;
        this.index = index;
        this.location = location;
        this.entity = NmsManager.spawnNPC(this.location, text);
        this.entity.setName(this.name);
    }

    public NPCLine(Location location) {
        this.location = location;
        this.name = "";
    }

    public NPCLine(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    public void sendPacket() {
        // Implemente o envio de pacotes relacionados ao NPC aqui
    }

    public int getIndex() {
        return this.index;
    }

    public String getText() {
        return this.name;
    }

    public void updateText(String text) {
        this.name = text;
        this.entity.setName(text);
    }

    public void destroy() {
        this.entity.kill();
        this.name = null;
        this.index = 0;
        this.location = null;
        this.entity = null;
    }

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return this.location;
    }
}
