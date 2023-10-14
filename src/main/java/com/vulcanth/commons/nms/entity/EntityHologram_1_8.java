package com.vulcanth.commons.nms.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.vulcanth.commons.nms.hologram.IHologramEntity;
import com.vulcanth.commons.nms.utils.NullBoundingBox;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

public class EntityHologram_1_8 extends EntityArmorStand implements IHologramEntity {

    public EntityHologram_1_8(Location location) {
        super(((CraftWorld) location.getWorld()).getHandle());
        setInvisible(true);
        setSmall(true);
        setArms(false);
        setGravity(true);
        setBasePlate(true);

        try {
            Field field = net.minecraft.server.v1_8_R3.EntityArmorStand.class.getDeclaredField("bi");
            field.setAccessible(true);
            field.set(this, 2147483647);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        a(new NullBoundingBox());
    }

    @Override
    public void setLocation(double x, double y, double z) {
        super.setPosition(x, y, z);

        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(getId(), MathHelper.floor(this.locX * 32.0D), MathHelper.floor(this.locY * 32.0D), MathHelper.floor(this.locZ * 32.0D),
                        (byte) (int) (this.yaw * 256.0F / 360.0F), (byte) (int) (this.pitch * 256.0F / 360.0F), this.onGround);

        for (EntityHuman obj : world.players) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer nmsPlayer = (EntityPlayer) obj;

                double distanceSquared = square(nmsPlayer.locX - this.locX) + square(nmsPlayer.locZ - this.locZ);
                if (distanceSquared < 8192.0 && nmsPlayer.playerConnection != null) {
                    nmsPlayer.playerConnection.sendPacket(teleportPacket);
                }
            }
        }
    }

    private static double square(double num) {
        return num * num;
    }

    public boolean isInvulnerable(DamageSource source) {
        return true;
    }

    public void setCustomName(String customName) {}

    public void setCustomNameVisible(boolean visible) {}

    public void t_() {
        this.ticksLived = 0;
    }

    public void makeSound(String sound, float f1, float f2) {}

    public void die() {
        super.die();
    }

    @Override
    public void setName(String text) {
        if (text != null && text.length() > 300) {
            text = text.substring(0, 300);
        }

        super.setCustomName(text == null ? "" : text);
        super.setCustomNameVisible(text != null && !text.isEmpty());
    }

    @Override
    public void kill() {
        super.die();
    }

    @Override
    public void sendPackets(Location location) {
        for (Player online : Bukkit.getOnlinePlayers().stream().filter(player -> player.getLocation().distance(location) <= 6).collect(Collectors.toList())) {
            CraftPlayer player = (CraftPlayer) online;
            PacketContainer spawnEntityPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
            spawnEntityPacket.getIntegers().write(0, this.getBukkitEntity().getEntityId());
            spawnEntityPacket.getIntegers().write(1, 30); // Substitua 30 pelo ID correto da entidade ArmaduraStand

            // Defina as coordenadas da entidade
            spawnEntityPacket.getDoubles().write(0, location.getX());
            spawnEntityPacket.getDoubles().write(1, location.getY());
            spawnEntityPacket.getDoubles().write(2, location.getZ());

            // Envie o pacote para o jogador
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnEntityPacket);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
