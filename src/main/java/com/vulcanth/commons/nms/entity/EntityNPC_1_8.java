package com.vulcanth.commons.nms.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.mojang.authlib.GameProfile;
import com.vulcanth.commons.Main;
import com.vulcanth.commons.library.NPCManager;
import com.vulcanth.commons.nms.npcs.INPCEntity;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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

    @Override
    public void setShowNick(boolean showNick) {
        Player p = this.getBukkitEntity();

        ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), p.getName());

        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

        ArrayList<String> playerToAdd = new ArrayList<>();

        for (Player online : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) online).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
            playerToAdd.add(p.getName());
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
        }
    }

    @Override
    public Player getPlayer() {
        return (Player) this.bukkitEntity;
    }
}