package com.vulcanth.commons.nms.collections;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.vulcanth.commons.Main;
import com.vulcanth.commons.nms.NMS;
import com.vulcanth.commons.nms.entity.EntityHologram_1_8;
import com.vulcanth.commons.nms.hologram.IHologramEntity;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMS_1_8 implements NMS {
    @Override
    public void sendAction(Player player, String message) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        IChatBaseComponent actionBarText = new ChatComponentText(message);
        PacketPlayOutChat packet = new PacketPlayOutChat(actionBarText, (byte) 2);
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        CraftPlayer craftPlayer = (CraftPlayer) player;

        craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(fadeIn, stay, fadeOut));
        craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}")));
        craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}")));
    }

    @Override
    public void sendCustomTab(Player player, String header, String footer) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header + "\"}"));

        try {
            java.lang.reflect.Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer + "\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void setValueAndSignature(Player player, String value, String signature) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        if (value != null && signature != null) {
            profile.getProperties().clear();
            profile.getProperties().put("textures", new Property("textures", value, signature));
        }
    }

    @Override
    public IHologramEntity spawnHologramEntity(Location location) {
        EntityHologram_1_8 entity = new EntityHologram_1_8(location);
        entity.setLocation(location.getX(), location.getY(), location.getZ());
        entity.getWorld().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        entity.setPosition(location.getX(), location.getY(), location.getZ());
        return entity;
    }

    @Override
    public void refreshPlayer(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();

        int entId = ep.getId();
        player.getLocation();

        PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(entId);
        PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);
        PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);
        PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(entId, 0, CraftItemStack.asNMSCopy(player.getItemInHand()));
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entId, 4, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
        PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entId, 3, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
        PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entId, 2, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entId, 1, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
        PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(player.getInventory().getHeldItemSlot());

        for (Player players : Bukkit.getOnlinePlayers()) {
            EntityPlayer epOn = ((CraftPlayer) players).getHandle();
            PlayerConnection con = epOn.playerConnection;
            if (players.equals(player)) {
                con.sendPacket(removeInfo);

                final boolean allow = player.getAllowFlight();
                final boolean flying = player.isFlying();
                final Location location = player.getLocation();
                final int level = player.getLevel();
                final float xp = player.getExp();
                final double maxHealth = player.getMaxHealth();
                final double health = player.getHealth();

                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    con.sendPacket(new PacketPlayOutRespawn(players.getWorld().getEnvironment().getId(), epOn.getWorld().getDifficulty(), epOn.getWorld().getWorldData().getType(),
                            epOn.playerInteractManager.getGameMode()));

                    player.setAllowFlight(allow);
                    if (flying) {
                        player.setFlying(allow);
                    }
                    player.teleport(location);
                    player.updateInventory();
                    player.setLevel(level);
                    player.setExp(xp);
                    player.setMaxHealth(maxHealth);
                    player.setHealth(health);
                    epOn.updateAbilities();

                    con.sendPacket(addInfo);
                }, 1L);
            } else {
                if (players.canSee(player) && players.getWorld().equals(player.getWorld())) {
                    con.sendPacket(removeEntity);
                    con.sendPacket(removeInfo);
                    con.sendPacket(addInfo);
                    con.sendPacket(addNamed);
                    con.sendPacket(itemhand);
                    con.sendPacket(helmet);
                    con.sendPacket(chestplate);
                    con.sendPacket(leggings);
                    con.sendPacket(boots);
                } else if (players.canSee(player)) {
                    con.sendPacket(removeInfo);
                    con.sendPacket(addInfo);
                }
            }
        }
    }
}
