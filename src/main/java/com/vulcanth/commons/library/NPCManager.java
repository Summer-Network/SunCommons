package com.vulcanth.commons.library;

import com.vulcanth.commons.library.npc.NPC;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class NPCManager {

    private static final List<NPC> NPCs = new ArrayList<>();

    public static NPC createNPC(String id, Location location) {
        NPC npc = new NPC(id, location);
        NPCs.add(npc);
        return npc;
    }

    public static NPC createNPC(Location location) {
        NPC npc = new NPC(location);
        NPCs.add(npc);
        return npc;
    }

    public static NPC createNPC(String id, Location location, String name) {
        NPC npc = new NPC(id, location, name);
        NPCs.add(npc);
        return npc;
    }

    public static NPC createNPC(Location location, String name) {
        NPC npc = new NPC(location, name);
        NPCs.add(npc);
        return npc;
    }

    public static void removeNPC(NPC npc) {
        if (npc != null) {
            NPCs.remove(npc);
            npc.destroy();
        }
    }

    public static NPC findByID(String id) {
        return NPCs.stream().filter(npc -> npc.getId().equals(id)).findFirst().orElse(null);
    }

    public static NPC findByLocation(Location location) {
        return NPCs.stream().filter(npc -> npc.getLocation().equals(location)).findFirst().orElse(null);
    }
}
