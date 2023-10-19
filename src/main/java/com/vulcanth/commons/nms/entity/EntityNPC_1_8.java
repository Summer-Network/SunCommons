package com.vulcanth.commons.nms.entity;

import com.mojang.authlib.GameProfile;
import com.vulcanth.commons.nms.npcs.INPCEntity;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class EntityNPC_1_8 extends EntityPlayer implements INPCEntity {
    private Location location;
    private final List<Player> playerPackets;

    public EntityNPC_1_8(WorldServer ws, GameProfile gp) {
        super(MinecraftServer.getServer(), ws, gp, new PlayerInteractManager(ws));
        this.location = null;
        this.playerPackets = new ArrayList<>();
    }

    @Override
    public void setName(String text) {
        this.setCustomName(text);
    }

    @Override
    public void kill() {
        super.die();
    }

    @Override
    public void setLocation(double x, double y, double z, World world) {
        this.location = new Location(world, x, y, z);
        this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void spawn() {
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);

        if (this.playerPackets.isEmpty()) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                sendPacket((CraftPlayer) online);
            }
            return;
        }

        for (Player online : this.playerPackets) {
            sendPacket((CraftPlayer) online);
        }
    }

    private void sendPacket(CraftPlayer online) {
        PlayerConnection connection = online.getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this, (byte) ((this.location.getYaw() * 256f) / 360f)));
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this));
    }

    @Override
    public void setItemInHand(ItemStack item) {
        for (Player p : this.playerPackets) {
            if (p.isOnline()) {
                PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutEntityEquipment(getId(), 0, CraftItemStack.asNMSCopy(item)));
            }
        }
    }

    @Override
    public void sendPackets(Player... players) {
        for (Player player : players) {
            if (!this.playerPackets.contains(player)) {
                this.playerPackets.add(player);
            }
        }
    }
}