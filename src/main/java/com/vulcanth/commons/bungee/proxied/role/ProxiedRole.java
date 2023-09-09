package com.vulcanth.commons.bungee.proxied.role;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;

public class ProxiedRole {

    public static ProxiedRoleEnum findRole(ProxiedPlayer player) {
        return Arrays.stream(ProxiedRoleEnum.values()).filter(roleEnum -> roleEnum.getPermission() != null && player.hasPermission(roleEnum.getPermission())).findFirst().orElse(ProxiedRoleEnum.MEMBRO);
    }
    public static ProxiedRoleEnum findRoleByID(String id) {
        return Arrays.stream(ProxiedRoleEnum.values()).filter(roleEnum -> String.valueOf(roleEnum.getId()).equals(id)).findFirst().orElse(ProxiedRoleEnum.MEMBRO);
    }
}
