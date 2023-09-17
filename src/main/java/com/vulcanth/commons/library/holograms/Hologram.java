package com.vulcanth.commons.library.holograms;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private final Location location;
    private final List<String> lines;
    private boolean spawned;
    private final List<Player> playersInRange;

    public Hologram(Location location, List<String> lines) {
        this.location = location;
        this.lines = new ArrayList<>(lines);
        this.spawned = false;
        this.playersInRange = new ArrayList<>();
    }


    public void spawn(Player player) {
        if (spawned) {
            return; // Já spawnado
        }

        for (int i = 0; i < lines.size(); i++) {
            Location lineLocation = location.clone().add(0, -i * 0.25, 0);
            String lineText = lines.get(i);
            HologramLine lineEntity = new HologramLine(this, lineLocation, lineText);
            lineEntity.spawn(player);
        }
        spawned = true;
    }

    public void despawn() {
        if (!spawned) {
            return; // Já despawnado
        }

        for (Player player : playersInRange) {
            for (HologramLine lineEntity : getLines()) {
                lineEntity.despawn(player);
            }
        }
        spawned = false;
    }

    public List<HologramLine> getLines() {
        List<HologramLine> lines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            Location lineLocation = location.clone().add(0, -i * 0.25, 0);
            String lineText = String.valueOf(lines.get(i));
            HologramLine lineEntity = new HologramLine(this, lineLocation, lineText);
            lines.add(lineEntity);
        }
        return lines;
    }

    public List<Player> getPlayersInRange() {
        return new ArrayList<>(playersInRange);
    }

    public void addPlayerInRange(Player player) {
        playersInRange.add(player);
    }

    public void removePlayerInRange(Player player) {
        playersInRange.remove(player);
    }
}
