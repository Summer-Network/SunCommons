package com.vulcanth.commons.bungee.proxied.role;

import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import com.vulcanth.commons.bungee.proxied.cache.collections.PlayerInformationsCache;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;

public class ProxiedRole {

    public static ProxiedRoleEnum findRole(ProxiedPlayer player) {
        ProxiedProfile profile = ProxiedProfile.loadProfile(player.getName());
        if (profile != null) {
            return findRoleByID(profile.getCache(PlayerInformationsCache.class).getInformation("role"));
        }

        return Arrays.stream(ProxiedRoleEnum.values()).filter(roleEnum -> roleEnum.getPermission() != null && player.hasPermission(roleEnum.getPermission())).findFirst().orElse(ProxiedRoleEnum.MEMBRO);
    }
    public static ProxiedRoleEnum findRole(ProxiedProfile player) {
        ProxiedProfile profile = ProxiedProfile.loadProfile(player.getName());
        if (profile != null) {
            return findRoleByID(profile.getCache(PlayerInformationsCache.class).getInformation("role"));
        }

        return Arrays.stream(ProxiedRoleEnum.values()).filter(roleEnum -> roleEnum.getPermission() != null && player.getPlayer().hasPermission(roleEnum.getPermission())).findFirst().orElse(ProxiedRoleEnum.MEMBRO);
    }
    public static ProxiedRoleEnum findRoleByID(String id) {
        return Arrays.stream(ProxiedRoleEnum.values()).filter(roleEnum -> String.valueOf(roleEnum.getId()).equals(id)).findFirst().orElse(ProxiedRoleEnum.MEMBRO);
    }
}
