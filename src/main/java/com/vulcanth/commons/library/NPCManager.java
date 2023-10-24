package com.vulcanth.commons.library;

import com.vulcanth.commons.library.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NPCManager {


    private static final List<NPC> NPC_CACHE = new ArrayList<>();

    public static NPC createNPC(String id, String name, Location location) {
        NPC npc = new NPC(id, name, location);
        NPC_CACHE.add(npc);
        return npc;
    }

    public static NPC createNPC(String name, Location location) {
        NPC npc = new NPC(location, name);
        NPC_CACHE.add(npc);
        return npc;
    }

    public static NPC createNPC(String id, String name, Location location, String value, String signature) {
        NPC npc = new NPC(id, name, location, value, signature);
        NPC_CACHE.add(npc);
        return npc;
    }

    public static NPC createNPC(String name, Location location, String value, String signature) {
        NPC npc = new NPC(location, name, value, signature);
        NPC_CACHE.add(npc);
        return npc;
    }

    public static void removeNPC(NPC npc) {
        if (npc != null) {
            NPC_CACHE.remove(npc);
            npc.destroy();
        }
    }

    public static NPC findByID(String id) {
        return NPC_CACHE.stream().filter(hologram -> hologram.getId().equals(id)).findFirst().orElse(null);
    }

    public static NPC findByLocation(Location location) {
        return NPC_CACHE.stream().filter(hologram -> hologram.getLocation().equals(location)).findFirst().orElse(null);
    }

    public static boolean isNPC(Player player) {
        return NPC_CACHE.stream().anyMatch(npc -> npc.getEntity().getPlayer().equals(player));
    }

    public static List<NPC> listNPC() {
        return NPC_CACHE;
    }

    public static List<NPC> listNPC(boolean onlyCanSee, Player player) {
        return NPC_CACHE.stream().filter(npc -> npc.canSee(player)).collect(Collectors.toList());
    }
}
