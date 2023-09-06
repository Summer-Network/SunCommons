package com.vulcanth.commons.bungee.proxied.role;

import com.vulcanth.commons.utils.tags.TagUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ProxiedRole {

    public static ProxiedRoleEnum findRole(ProxiedPlayer player) {
        return Arrays.stream(ProxiedRoleEnum.values()).filter(roleEnum -> roleEnum.getPermission() != null && player.hasPermission(roleEnum.getPermission())).findFirst().orElse(ProxiedRoleEnum.MEMBRO);
    }

    public static void reset(Player player) {
        TagUtils.reset(player.getName());
    }
}
