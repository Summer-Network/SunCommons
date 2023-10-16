package com.vulcanth.commons.library.npc;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class NPC {

    private String id;
    private Location location;
    private List<NPCLine> lines;

    public NPC(String id, Location location) {
        this.id = id;
        this.location = location;
        this.lines = new ArrayList<>();
    }

    public NPC(Location location) {
        this.id = "";
        this.location = location;
        this.lines = new ArrayList<>();
    }

    public NPC(Location location, String... names) {
        this.id = "";
        this.location = location;
        this.lines = new ArrayList<>();

        for (String name : names) {
            addNPC(name);
        }
    }

    public NPC(String id, Location location, String... names) {
        this.id = id;
        this.location = location;
        this.lines = new ArrayList<>();

        for (String name : names) {
            addNPC(name);
        }
    }

    public void destroy() {
        this.location = null;
        this.id = null;
        this.lines.forEach(NPCLine::destroy);
        this.lines = null;
    }

    public NPCLine findByIndex(int index) {
        return lines.stream().filter(npcLine -> npcLine.getIndex() == index).findFirst().orElse(null);
    }

    public void addNPC(String name) {
        this.lines.add(new NPCLine(name, this.lines.size(), this.location));
    }

    public String getId() {
        return this.id;
    }

    public List<NPCLine> getLines() {
        return this.lines;
    }

    public Location getLocation() {
        return this.location;
    }
}
