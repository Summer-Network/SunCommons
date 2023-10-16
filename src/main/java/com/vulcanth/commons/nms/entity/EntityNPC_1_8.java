package com.vulcanth.commons.nms.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.vulcanth.commons.nms.npcs.INPCEntity;
import com.vulcanth.commons.nms.utils.NullBoundingBox;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntityNPC_1_8 extends EntityPlayer implements INPCEntity {
    public EntityNPC_1_8(Location location, String name) {
        super(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) location.getWorld()).getHandle(), ((CraftPlayer) Bukkit.getOnlinePlayers().stream().findAny().orElse(null)).getHandle().getProfile(), new PlayerInteractManager(((CraftWorld) location.getWorld()).getHandle()));

        setPosition(location.getX(), location.getY(), location.getZ());

        this.a(new NullBoundingBox());
        this.locX = location.getX();
        this.locY = location.getY();
        this.locZ = location.getZ();

        setInvisible(false);

        setCustomName(name);
        setCustomNameVisible(true);
    }


    @Override
    public void setLocation(double x, double y, double z) {
        setPosition(x, y, z);
    }

    public boolean isInvulnerable(DamageSource source) {
        return true;
    }

    @Override
    public void setName(String text) {
        super.setCustomName(text);
    }

    @Override
    public void kill() {
        super.die();
    }

    @Override
    public void sendPackets(Location location) {
        for (Player online : Bukkit.getOnlinePlayers().stream().filter(player -> player.getLocation().distance(location) <= 6).collect(Collectors.toList())) {
            CraftPlayer player = (CraftPlayer) online;
            PacketContainer spawnPlayerPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);

            spawnPlayerPacket.getIntegers().write(0, this.getId());
            spawnPlayerPacket.getUUIDs().write(0, this.getUniqueID());
            spawnPlayerPacket.getDoubles().write(0, this.locX);
            spawnPlayerPacket.getDoubles().write(1, this.locY);
            spawnPlayerPacket.getDoubles().write(2, this.locZ);
            spawnPlayerPacket.getBytes().write(0, (byte) ((int) this.yaw * 256.0F / 360.0F));
            spawnPlayerPacket.getBytes().write(1, (byte) ((int) this.pitch * 256.0F / 360.0F));

            // Configurando o DataWatcher e ChatComponent
            WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher((org.bukkit.entity.Entity) EntityNPC_1_8.this);
            watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), WrappedChatComponent.fromText(getName()));
            spawnPlayerPacket.getDataWatcherModifier().write(0, watcher);

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnPlayerPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
