package com.vulcanth.commons.player.role;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.utils.tags.TagUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Role {

    public static RoleEnum findRole(Player player) {
        return Arrays.stream(RoleEnum.values()).filter(roleEnum -> roleEnum.getPermission() != null && player.hasPermission(roleEnum.getPermission())).findFirst().orElse(RoleEnum.MEMBRO);
    }

    public static RoleEnum findRole(ProxiedPlayer player) {
        return Arrays.stream(RoleEnum.values()).filter(roleEnum -> roleEnum.getPermission() != null && player.hasPermission(roleEnum.getPermission())).findFirst().orElse(RoleEnum.MEMBRO);
    }

    public static void setTag(Player player) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), ()-> {
            TagUtils.sendTeams(player);
            TagUtils.setTag(player);
        }, 3L);
    }

    public static void reset(Player player) {
        TagUtils.reset(player.getName());
    }
}
