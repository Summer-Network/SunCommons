package com.vulcanth.commons.nms;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.library.holograms.Hologram;
import com.vulcanth.commons.nms.collections.IEntityWrapper;
import com.vulcanth.commons.nms.collections.NMS_1_8;
import com.vulcanth.commons.library.holograms.HologramLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NmsManager {

    private static NMS nms;

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

    public static void setupNMS() {
        // Load no NMS reconrrente a versão do plugin
        String concurrentVersion = getVersion();
        if (concurrentVersion.contains("1.8")) {
            nms = new NMS_1_8();
        } else {
            Main.getInstance().sendMessage("O plugin atualmente não possui suporte a versão " + concurrentVersion + ".");
            Bukkit.shutdown();
        }
    }

    private static String getVersion() {
        return Bukkit.getServer().getVersion().split("MC: ")[1].split("\\)")[0];
    }

    public static IEntityWrapper createArmorStand(Location location, HologramLine line, ItemStack item) {
        return nms.createArmorStand(location, line, item);
    }
    public static IEntityWrapper createItem(Location location, HologramLine line, ItemStack item) {
        return nms.createItem(location, line, item);
    }

    public static IEntityWrapper createSlime(Location location, HologramLine line) {
        return nms.createSlime(location, line);
    }
}
