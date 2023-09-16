package com.vulcanth.commons.library.holograms;

import org.bukkit.Chunk;

public class HologramCoord {
    public final String world;
    private final int x;
    private final int z;

    public HologramCoord(Chunk chunk) {
        this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    public HologramCoord(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HologramCoord)) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        HologramCoord other = (HologramCoord) obj;
        if (world == null) {
            return other.world == null;
        } else {
            return world.equals(other.world) && x == other.x && z == other.z;
        }
    }
}
