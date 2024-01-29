package com.summer.commons.model;

import com.summer.commons.player.role.RoleEnum;
import com.summer.commons.utils.BukkitUtils;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum UpgradeVIP {

    MVP(RoleEnum.MVP, RoleEnum.VIP, 5000L, BukkitUtils.getItemStackFromString("35:1 : 1 : nome>&6Tornar-se MVP : desc>§7Clique aqui para confirmar a evolução\n§7de seu VIP para o VIP MVP \n§7utilizando seu saldo em Cash.\n \n§7Você gastará §65000 cash.")),
    MVPPLUS(RoleEnum.MVP, RoleEnum.VIP, 8000L, BukkitUtils.getItemStackFromString("35:9 : 1 : nome>&bTornar-se MVP&6+ : desc>§7Clique aqui para confirmar a evolução\n§7de seu VIP para o VIP MVP+ \n§7utilizando seu saldo em Cash.\n \n§7Você gastará §68000 cash."));

    public static UpgradeVIP findUpgradeVIP(RoleEnum role) {
        return Arrays.stream(UpgradeVIP.values()).filter(upgradeVIP -> upgradeVIP.getRole().getId() == role.getId() - 1).findFirst().orElse(null);
    }

    private final RoleEnum role;
    private final RoleEnum roleNecessary;
    private final Long price;
    private final ItemStack item;

    UpgradeVIP(RoleEnum role, RoleEnum roleNecessary, Long price, ItemStack item) {
        this.role = role;
        this.roleNecessary = roleNecessary;
        this.price = price;
        this.item = item;
    }

    public RoleEnum getRole() {
        return this.role;
    }

    public RoleEnum getRoleNecessary() {
        return this.roleNecessary;
    }

    public Long getPrice() {
        return this.price;
    }

    public ItemStack getIcon() {
        return this.item;
    }
}
