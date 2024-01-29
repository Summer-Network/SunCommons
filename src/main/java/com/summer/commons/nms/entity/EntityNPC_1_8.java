package com.summer.commons.nms.entity;

import com.mojang.authlib.GameProfile;
import com.summer.commons.nms.NMS;
import com.summer.commons.nms.NmsManager;
import com.summer.commons.nms.npcs.INPCEntity;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EntityNPC_1_8 extends EntityPlayer implements INPCEntity {
    private Location location;
    private final List<Player> playerPackets;
    private boolean showNick = true;

    public EntityNPC_1_8(WorldServer ws, GameProfile gp) {
        super(MinecraftServer.getServer(), ws, gp, new PlayerInteractManager(ws));
        this.location = null;
        this.playerPackets = new ArrayList<>();
        this.getBukkitEntity().setRemoveWhenFarAway(false);
    }

    @Override
    public void setName(String text) {
        this.setCustomName(text);
    }

    @Override
    public void kill() {
        super.die();
        PacketPlayOutPlayerInfo pack = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getBukkitEntity().getHandle());
        if (playerPackets.isEmpty()) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.isOnline()) {
                    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(pack);
                }
            }

            return;
        }

        for (Player online : playerPackets) {
            if (online.isOnline()) {
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(pack);
            }
        }

    }

    @Override
    public void setLocation(World world, double x, double y, double z) {
        this.location = new Location(world, x, y, z);
        this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void spawn() {
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        if (this.playerPackets.isEmpty()) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.isOnline()) {
                    sendPacket(online);
                    online.hidePlayer(this.getPlayer());
                    online.showPlayer(this.getPlayer());
                }
            }
            return;
        }

        for (Player online : this.playerPackets) {
            if (online.isOnline()) {
                sendPacket(online);
                online.hidePlayer(this.getPlayer());
                online.showPlayer(this.getPlayer());
            }
        }
    }
    private void sendPacket(Player online) {
        PlayerConnection connection = ((CraftPlayer)online).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this));
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(this.getId(), (byte) ((yaw%360.)*256/360), (byte) ((pitch%360.)*256/360), false));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this, (byte) ((yaw%360.)*256/360)));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this));
        DataWatcher dw = new DataWatcher(null);
        dw.a(10, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
        connection.sendPacket(new PacketPlayOutEntityMetadata(this.getId(), dw, true));
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this));
        Player p = getPlayer();
        p.sendMessage("yam: " + (byte) ((yaw%360.)*256/360) + "\npitch: " + (byte) ((pitch%360.)*256/360));
        if (!showNick) {
            ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), p.getName());
            team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

            ArrayList<String> playerToAdd = new ArrayList<>();
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
            playerToAdd.add(p.getName());
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
        }
        NMS.setHeadYaw(this.getBukkitEntity(), yaw);
        NmsManager.look(this.getBukkitEntity(), this.getPlayer().getLocation().getYaw(), this.getPlayer().getLocation().getPitch());
    }


    public void sendPackets(CraftPlayer online) {
        PlayerConnection connection = online.getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this, (byte) ((this.location.getYaw() * 256.0F) / 360.0F)));
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this));
        if (!showNick) {
            Player p = getPlayer();
            ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), p.getName());
            team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

            ArrayList<String> playerToAdd = new ArrayList<>();
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
            playerToAdd.add(p.getName());
            connection.sendPacket(new PacketPlayOutScoreboardTeam(team, playerToAdd, 3));
        }

        NmsManager.look(this.getBukkitEntity(), location.getYaw(), location.getPitch());
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
        if (!showNick) {
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

        this.showNick = showNick;
    }

    @Override
    public Player getPlayer() {
        return this.getBukkitEntity();
    }

    @Override
    public void update() {
        if (playerPackets.isEmpty()) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.isOnline()) {
                    sendPacket(online);
                }
            }

            return;
        }

        for (Player online : playerPackets) {
            if (online.isOnline()) {
                sendPacket(online);
            }
        }
    }

    @Override
    public void update(Player player) {
        sendPacket(player);
        player.hidePlayer(getPlayer());
        player.showPlayer(getPlayer());
    }

    @Override
    public List<Player> packetsPlayer() {
        return this.playerPackets;
    }
}