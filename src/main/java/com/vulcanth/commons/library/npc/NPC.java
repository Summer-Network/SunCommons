package com.vulcanth.commons.library.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.UUID;

public class NPC {

    private String id;
    private Location location;
    private String name;
    private EntityPlayer npc;

    public NPC(String id, Location location) {
        this.id = id;
        this.location = location;
        this.name = "";
        createNPC();
    }

    public NPC(Location location) {
        this.id = "";
        this.location = location;
        this.name = "";
        createNPC();
    }

    public NPC(Location location, String name) {
        this.id = "";
        this.location = location;
        this.name = name;
        createNPC();
    }

    public NPC(String id, Location location, String name) {
        this.id = id;
        this.location = location;
        this.name = name;
        createNPC();
    }

    public void destroy() {
        removeNPC();
    }

    public String getId() {
        return this.id;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        updateName();
    }

    private void createNPC() {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

        GameProfile profile = new GameProfile(UUID.randomUUID(), name);

        npc = new EntityPlayer(server, worldServer, profile, new PlayerInteractManager(worldServer));
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        int entityId = npc.getId();
        PacketPlayOutPlayerInfo addInfoPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc);
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(npc);
        sendPackets(addInfoPacket, spawnPacket);
        worldServer.addEntity(npc, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    private void removeNPC() {
        if (npc != null) {
            WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
            PacketPlayOutPlayerInfo removeInfoPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc);
            PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(npc.getId());
            sendPackets(removeInfoPacket, destroyPacket);
            worldServer.removeEntity(npc);
        }
    }

    private void sendPackets(Packet<?>... packets) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            for (Packet<?> packet : packets) {
                connection.sendPacket(packet);
            }
        }
    }

    private void updateName() {
        if (npc != null) {
            DataWatcher dataWatcher = npc.getDataWatcher();
            dataWatcher.watch(10, name);
            PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(npc.getId(), dataWatcher, true);
            sendPackets(metadataPacket);
        }
    }
}
