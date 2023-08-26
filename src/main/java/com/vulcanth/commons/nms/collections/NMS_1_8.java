package com.vulcanth.commons.nms.collections;

import com.vulcanth.commons.nms.NMS;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

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

        IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        IChatBaseComponent subtitleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");

        PacketPlayOutChat titlePacket = new PacketPlayOutChat(titleComponent, (byte) 2);
        PacketPlayOutChat subtitlePacket = new PacketPlayOutChat(subtitleComponent, (byte) 2);

        craftPlayer.getHandle().playerConnection.sendPacket(titlePacket);
        craftPlayer.getHandle().playerConnection.sendPacket(subtitlePacket);

        PacketPlayOutTitle timingsPacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
        craftPlayer.getHandle().playerConnection.sendPacket(timingsPacket);
    }

    @Override
    public void sendCustonTab(Player player, String header, String footer) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header + "\"}"));

        try {
            java.lang.reflect.Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + "\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }
}
