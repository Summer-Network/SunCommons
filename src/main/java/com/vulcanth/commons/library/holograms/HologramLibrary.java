package com.vulcanth.commons.library.holograms;

import com.vulcanth.commons.library.HologramAbstract;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class HologramLibrary extends HologramAbstract {

    private static final List<Hologram> holograms = new ArrayList<>();
    private static final Map<Integer, Hologram> entityIdToHologramMap = new HashMap<>();

    public static Hologram createHologram(Location location, List<String> lines) {
        return createHologram(location, lines);
    }

    public static Hologram createHologram(Location location, String... lines) {
        return createHologram(location, true, lines);
    }

    public static Hologram createHologram(Location location, boolean spawn, String[] lines) {
        Hologram hologram = new Hologram(location, Arrays.asList(lines));
        Player player = null;
        if (spawn) {
            hologram.spawn(player);
        }
        holograms.add(hologram);
        return hologram;
    }

    public static void removeHologram(Hologram hologram) {
        holograms.remove(hologram);
        hologram.despawn();
    }

    public static void unregisterAll() {
        for (Hologram hologram : holograms) {
            hologram.despawn();
        }
        holograms.clear();
        entityIdToHologramMap.clear();
    }

    public static Hologram getHologram(int entityId) {
        return entityIdToHologramMap.get(entityId);
    }

    public static Collection<Hologram> listHolograms() {
        return new ArrayList<>(holograms);
    }

    public static void updateHologramsForPlayer(Player player) {
        for (Hologram hologram : holograms) {
            for (HologramLine line : hologram.getLines()) {
                line.spawn(player);
            }
        }
    }

    public static void hideHologramsForPlayer(Player player) {
        for (Hologram hologram : holograms) {
            for (HologramLine line : hologram.getLines()) {
                line.despawn(player);
            }
        }
    }


    public static void showHologramsForAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateHologramsForPlayer(player);
        }
    }

    public static void hideHologramsForAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            hideHologramsForPlayer(player);
        }
    }
}
