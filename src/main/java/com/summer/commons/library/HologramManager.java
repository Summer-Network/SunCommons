package com.summer.commons.library;

import com.summer.commons.library.hologram.Hologram;
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

    public static void removeNPC(Hologram hologram) {
        if (hologram != null) {
            HOLOGRAMS.remove(hologram);
            hologram.destroy();
        }
    }

    public static Hologram findByID(String id) {
        return HOLOGRAMS.stream().filter(hologram -> hologram.getId().equals(id)).findFirst().orElse(null);
    }

    public static Hologram findByLocation(Location location) {
        return HOLOGRAMS.stream().filter(hologram -> hologram.getLocation().equals(location)).findFirst().orElse(null);
    }
}
