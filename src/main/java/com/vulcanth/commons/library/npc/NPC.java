package com.vulcanth.commons.library.npc;

import com.vulcanth.commons.nms.NmsManager;
import com.vulcanth.commons.nms.npcs.INPCEntity;
import org.bukkit.Location;

public class NPC {

    private String id;
    private Location location;
    private INPCEntity entity;
    private String value;
    private String signature;
    private String npcName;

    public NPC(String id, String name, Location location) {
        this.id = id;
        this.npcName = name;
        this.location = location;
        this.entity = null;
        this.value = null;
        this.signature = null;
    }

    public NPC(Location location, String name) {
        this.id = "";
        this.npcName = name;
        this.location = location;
        this.entity = null;
        this.value = null;
        this.signature = null;
    }

    public NPC(String id, String name, Location location, String value, String signature) {
        this.id = id;
        this.npcName = name;
        this.location = location;
        this.entity = null;
        this.value = value;
        this.signature = signature;
    }

    public NPC(Location location, String name, String value, String signature) {
        this.id = "";
        this.npcName = name;
        this.location = location;
        this.entity = null;
        this.value = value;
        this.signature = signature;
    }

    public void destroy() {
        this.entity.kill();
        this.entity = null;
        this.npcName = null;
        this.id = null;
        this.value = null;
        this.signature = null;
        this.location = null;
    }

    public void createEntity() {
        if (this.value == null || this.signature == null) {
            this.entity = NmsManager.spawnNPCEntity(this.location, this.npcName);
            return;
        }

        this.entity = NmsManager.spawnNPC(this.location, this.npcName, this.value , this.signature);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setNpcName(String name) {
        this.npcName = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getId() {
        return this.id;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getValue() {
        return this.value;
    }

    public String getSignature() {
        return this.signature;
    }

    public INPCEntity getEntity() {
        return this.entity;
    }

    public String getNpcName() {
        return this.npcName;
    }
}
