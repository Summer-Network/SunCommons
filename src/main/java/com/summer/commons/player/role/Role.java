package com.summer.commons.player.role;

import com.summer.commons.Main;
import com.summer.commons.player.cache.collections.PlayerInformationsCache;
import com.summer.commons.player.Profile;
import com.summer.commons.utils.tags.TagUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Role {

    public static RoleEnum findRole(Player player) {
        Profile profile = Profile.loadProfile(player.getName());
        if (profile != null) {
            try {
                return findRoleByID(profile.getCache(PlayerInformationsCache.class).getInformation("role"));
            } catch (Exception e) {
                return RoleEnum.MEMBRO;
            }
        }

        return Arrays.stream(RoleEnum.values()).filter(roleEnum -> roleEnum.getPermission() != null && player.hasPermission(roleEnum.getPermission())).findFirst().orElse(RoleEnum.MEMBRO);
    }

    public static void setTag(Player player) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), ()-> {
            TagUtils.sendTeams(player);
            TagUtils.setTag(player);
        }, 3L);
    }

    public static RoleEnum findRoleByID(String id) {
        return Arrays.stream(RoleEnum.values()).filter(roleEnum -> String.valueOf(roleEnum.getId()).equals(id)).findFirst().orElse(RoleEnum.MEMBRO);
    }

    public static void reset(Player player) {
        TagUtils.reset(player.getName());
    }
}
