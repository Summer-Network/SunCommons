package com.vulcanth.commons.nms.entity;

import com.mojang.authlib.GameProfile;
import com.vulcanth.commons.nms.npcs.INPCEntity;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class EntityNPC_1_8 extends EntityPlayer {
    private final Location loc;

    public EntityNPC_1_8(WorldServer ws, GameProfile gp, Location loc) {
        super(MinecraftServer.getServer(), ws, gp, new PlayerInteractManager(ws));
        this.loc = loc;
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public void spawn() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            spawnFor(pl);
        }
    }

    public void spawnFor(Player p) {
        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;

        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this, (byte) ((loc.getYaw() * 256f) / 360f)));
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this));
        // connection.sendPacket(new PacketPlayOutEntityEquipment(getId(), 0, CraftItemStack.asNMSCopy(itemInHand)));
    }

    public void remove() {
        this.die();
    }

    public boolean isEntity(Entity et) {
        return this.getId() == et.getId();
    }
}