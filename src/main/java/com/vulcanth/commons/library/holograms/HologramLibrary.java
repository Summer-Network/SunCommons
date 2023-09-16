package com.vulcanth.commons.library.holograms;

import com.google.common.collect.ImmutableList;
import com.vulcanth.commons.plugin.VulcanthPlugins;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HologramLibrary {

    private static final List<Hologram> holograms = new ArrayList<>();
    private static Plugin plugin;

    public static Hologram createHologram(Location location, List<String> lines) {
        return createHologram(location, true, lines);
    }

    public static Hologram createHologram(Location location, String... lines) {
        return createHologram(location, true, lines);
    }

    public static Hologram createHologram(Location location, boolean spawn, String... lines) {
        Hologram hologram = new Hologram(location, lines);
        if (spawn) {
            hologram.spawn();
        }
        holograms.add(hologram);
        return hologram;
    }

    public static void removeHologram(Hologram hologram) {
        holograms.remove(hologram);
        hologram.despawn();
    }

    public static void unregisterAll() {
        holograms.forEach(Hologram::despawn);
        holograms.clear();
        plugin = null;
    }

    public static Entity getHologramEntity(int entityId) {
        for (Hologram hologram : listHolograms()) {
            if (hologram.isSpawned()) {
                for (HologramLine line : hologram.getLines()) {
                    IArmorStand armor = line.getArmor();
                    if (armor != null && armor.getId() == entityId) {
                        return armor.getEntity();
                    }
                }
            }
        }
        return null;
    }

    public static Hologram getHologram(Entity entity) {
        return NMS.getHologram(entity);
    }

    public static boolean isHologramEntity(Entity entity) {
        return NMS.isHologramEntity(entity);
    }

    public static Collection<Hologram> listHolograms() {
        return ImmutableList.copyOf(holograms);
    }
}
