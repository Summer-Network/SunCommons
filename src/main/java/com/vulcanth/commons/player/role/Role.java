package com.vulcanth.commons.player.role;

import org.bukkit.entity.Player;

import java.util.Arrays;

public class Role {

    public static RoleEnum findRole(Player player) {
        return Arrays.stream(RoleEnum.values()).filter(roleEnum -> player.hasPermission(roleEnum.getPermission())).findFirst().orElse(RoleEnum.MEMBRO);
    }
}
