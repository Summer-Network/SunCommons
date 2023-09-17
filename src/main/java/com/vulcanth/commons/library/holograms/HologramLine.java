package com.vulcanth.commons.library.holograms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.vulcanth.commons.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

@Getter
public class HologramLine {

    private final Location location;
    private InteractionHandler interactionHandler;
    private String line;
    private final Hologram hologram;
    private int entityId;

    public HologramLine(Hologram hologram, Location location, String line) {
        this.location = location;
        this.hologram = hologram;
        this.line = line;
    }

    public void spawn(Player player) {
        sendSpawnPacket(player);
    }

    public void despawn(Player player) {
        sendDestroyPacket(player);
    }

    public void setInteractionHandler(InteractionHandler interactionHandler) {
        this.interactionHandler = interactionHandler;
    }

    public void setLine(String line) {
        String formattedLine = StringUtils.formatColors(line);
        if (!formattedLine.equals(this.line)) {
            this.line = formattedLine;
            sendMetadataPacket();
        }
    }

    private void sendSpawnPacket(Player player) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        packet.getIntegers().write(0, entityId);
        packet.getIntegers().write(1, (int) (location.getX() * 32D));
        packet.getIntegers().write(2, (int) (location.getY() * 32D));
        packet.getIntegers().write(3, (int) (location.getZ() * 32D));
        packet.getBytes().write(0, (byte) 0);
        packet.getBytes().write(1, (byte) 0);
        packet.getBytes().write(2, (byte) 0);
        packet.getIntegers().write(4, 0);
        packet.getIntegers().write(5, 0);
        packet.getIntegers().write(6, 0);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void sendDestroyPacket(Player player) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getIntegerArrays().write(0, new int[]{entityId});

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private void sendMetadataPacket() {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entityId);

        // Preencha aqui os metadados necessários, como nome e outros atributos
        // Você pode usar packet.getWatchableCollectionModifier() para adicionar metadados

        for (Player player : hologram.getPlayersInRange()) {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
