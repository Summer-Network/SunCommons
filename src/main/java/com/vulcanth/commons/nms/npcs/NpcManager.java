package com.vulcanth.commons.nms.npcs;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NpcManager {
    public static void createNPC(Player player, String npcName) {
        try {
            MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
            WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();
            GameProfile profile = new GameProfile(java.util.UUID.randomUUID(), npcName);

            EntityPlayer npc = new EntityPlayer(server, worldServer, profile, new PlayerInteractManager(worldServer));
            npc.setLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

            int entityId = npc.getId();

            PacketPlayOutPlayerInfo addInfoPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc);
            PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(npc);

            sendPackets(player, spawnPacket, addInfoPacket);

            worldServer.addEntity(npc, CreatureSpawnEvent.SpawnReason.CUSTOM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeNPC(Player player, String npcName) {
        try {
            WorldServer world = ((CraftWorld) player.getWorld()).getHandle();

            for (Entity entity : world.entityList) {
                if (entity instanceof EntityPlayer) {
                    EntityPlayer npc = (EntityPlayer) entity;
                    if (npc.getProfile().getName().equals(npcName)) {
                        PacketPlayOutPlayerInfo removeInfoPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc);

                        sendPackets(player, removeInfoPacket);

                        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(npc.getId());
                        sendPackets(player, destroyPacket);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPackets(Player player, Packet<?>... packets) {
        for (Packet<?> packet : packets) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(packet);
        }
    }
}
