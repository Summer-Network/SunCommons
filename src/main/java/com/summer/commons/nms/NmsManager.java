package com.summer.commons.nms;

import com.summer.commons.nms.collections.NMS_1_8;
import com.summer.commons.nms.hologram.IHologramEntity;
import com.summer.commons.nms.npcs.INPCEntity;
import com.summer.commons.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NmsManager {

    private static NMS nms;

    public static void setupNMS() {
        String concurrentVersion = getVersion();
        if (concurrentVersion.contains("1.8")) {
            nms = new NMS_1_8();
        } else {
            Main.getInstance().sendMessage("O plugin atualmente não possui suporte a versão " + concurrentVersion + ".");
            Bukkit.shutdown();
        }
    }

    public static void setValueAndSignature(Player player, String value, String signature) {
        nms.setValueAndSignature(player, value, signature);
    }

    public static void sendAction(Player player, String message) {
        nms.sendAction(player, message);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        nms.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void sendCustomTab(Player player, String header, String footer) {
        nms.sendCustomTab(player, header, footer);
    }

    public static void refreshPlayer(Player player) {
        nms.refreshPlayer(player);
    }

    public static IHologramEntity spawnHologram(Location location) {
        return nms.spawnHologramEntity(location);
    }

    public static INPCEntity spawnNPC(Location location, String name, String value, String signature) {
        return nms.spawnNPCEntity(location, name, value, signature);
    }

    public static INPCEntity spawnNPCEntity(Location location, String npcName) {
        return nms.spawnNPCEntity(location, npcName);
    }

    public static void look(org.bukkit.entity.Entity entity, float yaw, float pitch) {
        nms.look(entity, yaw, pitch);
    }

    public static void setHeadYaw(org.bukkit.entity.Entity entity, float yaw) {
        NMS.setHeadYaw(entity, yaw);
    }

    private static String getVersion() {
        return Bukkit.getServer().getVersion().split("MC: ")[1].split("\\)")[0];
    }
}
