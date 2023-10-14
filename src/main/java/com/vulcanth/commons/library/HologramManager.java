package com.vulcanth.commons.library;

import com.vulcanth.commons.library.hologram.Hologram;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {

    private static final List<Hologram> HOLOGRAMS = new ArrayList<>();

    public static Hologram createHologram(String id, Location location) {
        Hologram hologram = new Hologram(id, location);
        HOLOGRAMS.add(hologram);
        return hologram;
    }

    public static Hologram createHologram(Location location) {
        Hologram hologram = new Hologram(location);
        HOLOGRAMS.add(hologram);
        return hologram;
    }

    public static Hologram createHologram(Location location, String... lines) {
        Hologram hologram = new Hologram(location, lines);
        HOLOGRAMS.add(hologram);
        return hologram;
    }

    public static Hologram createHologram(String id, Location location, String... lines) {
        Hologram hologram = new Hologram(id, location, lines);
        HOLOGRAMS.add(hologram);
        return hologram;
    }

    public static Hologram findByID(String id) {
        return HOLOGRAMS.stream().filter(hologram -> hologram.getId().equals(id)).findFirst().orElse(null);
    }

    public static Hologram findByLocation(Location location) {
        return HOLOGRAMS.stream().filter(hologram -> hologram.getLocation().equals(location)).findFirst().orElse(null);
    }
}
