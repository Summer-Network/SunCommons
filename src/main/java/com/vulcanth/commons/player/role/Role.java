package com.vulcanth.commons.player.role;

import com.vulcanth.commons.utils.tags.TagUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Role {

    public static RoleEnum findRole(Player player) {
        return Arrays.stream(RoleEnum.values()).filter(roleEnum -> player.hasPermission(roleEnum.getPermission())).findFirst().orElse(RoleEnum.MEMBRO);
    }

    public static void setTag(Player player) {
        TagUtils.sendTeams(player);
        TagUtils.setTag(player);
    }
}
