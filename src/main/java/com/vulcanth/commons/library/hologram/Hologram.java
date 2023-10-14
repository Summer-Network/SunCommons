package com.vulcanth.commons.library.hologram;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private String id;
    private Location location;
    private List<HologramLine> lines;


    public Hologram(String id, Location location) {
        this.location = location.subtract(0, 1.3, 0);
        this.id = id;
        this.lines = new ArrayList<>();
    }

    public Hologram(Location location) {
        this.location = location.subtract(0, 1.3, 0);
        this.id = "";
        this.lines = new ArrayList<>();
    }

    public Hologram(Location location, String... lines) {
        this.location = location.subtract(0, 1.3, 0);
        this.id = "";
        this.lines = new ArrayList<>();

        for (String line : lines) {
            addNewLine(line);
        }
    }

    public Hologram(String id, Location location, String... lines) {
        this.location = location.subtract(0, 1.3, 0);
        this.id = id;
        this.lines = new ArrayList<>();

        for (String line : lines) {
            addNewLine(line);
        }
    }

    public void destroy() {
        this.location = null;
        this.id = null;
        this.lines.forEach(HologramLine::destroy);
        this.lines = null;
    }

    public HologramLine findByIndex(int index) {
        return lines.stream().filter(hologramLine -> hologramLine.getIndex() == index).findFirst().orElse(null);
    }

    public void addNewLine(String line) {
        this.lines.add(new HologramLine(line, this.lines.size(), this.location.add(0, 0.3, 0)));
    }

    public String getId() {
        return this.id;
    }

    public List<HologramLine> getLines() {
        return this.lines;
    }

    public Location getLocation() {
        return this.location;
    }
}
