package com.vulcanth.commons.nms.collections;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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

    public void setValueAndSignature(Player player, String value, String signature) {
        GameProfile profile = ((CraftPlayer)player).getProfile();
        if (value != null && signature != null) {
            profile.getProperties().clear();
            profile.getProperties().put("textures", new Property("textures", value, signature));
        }
    }
}
