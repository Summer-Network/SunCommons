package com.summer.commons.nms.entity;

import com.mojang.authlib.GameProfile;
import com.summer.commons.nms.NMS;
import com.summer.commons.nms.NmsManager;
import com.summer.commons.nms.npcs.INPCEntity;
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
import org.bukkit.util.Vector;

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
        NmsManager.look(this.getBukkitEntity(), -90, this.getPlayer().getLocation().getPitch());
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

//    public void updateNpcHeadRotation(EntityLiving npc, double radius) {
//        // Certifique-se de que a entidade é uma instância de EntityLiving (um NPC é uma subclasse de EntityLiving)
//        if (!(npc instanceof EntityLiving)) {
//            return;
//        }
//
//        EntityLiving livingEntity = (EntityLiving) npc;
//
//        // Obtenha a localização do NPC
//        Location npcLocation = new Location(livingEntity.getWorld().getWorld(), livingEntity.locX, livingEntity.locY, livingEntity.locZ);
//
//        // Encontre jogadores na área
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            Location playerLocation = player.getLocation();
//
//            // Verifique se o jogador está dentro do raio
//            if (npcLocation.distanceSquared(playerLocation) <= radius * radius) {
//                // Obtenha a direção para o jogador
//                Vector direction = playerLocation.toVector().subtract(npcLocation.toVector()).normalize();
//
//                // Converta a direção para ângulos de rotação
//                float yaw = (float) Math.toDegrees(Math.atan2(direction.getZ(), direction.getX()));
//                float pitch = (float) Math.toDegrees(Math.asin(direction.getY()));
//
//                // Ajuste a rotação da cabeça do NPC
//                livingEntity.yaw = yaw - 90;  // -90 para ajustar a orientação
//                livingEntity.pitch = pitch;
//
//                // Se necessário, envie um pacote para atualizar a visão do NPC
//                PacketPlayOutEntity.PacketPlayOutEntityLook packet = new PacketPlayOutEntity.PacketPlayOutEntityLook(
//                        livingEntity.getId(), (byte) MathHelper.d(yaw * 256.0F / 360.0F),
//                        (byte) MathHelper.d(pitch * 256.0F / 360.0F), true
//                );
//
//                // Envie o pacote para todos os jogadores na vizinhança do NPC
//                ((CraftServer) Bukkit.getServer()).getHandle().sendPacketNearby(
//                        livingEntity, ((CraftWorld) livingEntity.getWorld()).getHandle(), packet
//                );
//
//                // Apenas ajuste para o jogador mais próximo e, em seguida, saia do loop
//                break;
//            }
//        }
//    }



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