package com.vulcanth.commons.model;

import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.collections.PlayerDeliveryCache;
import com.vulcanth.commons.player.cash.CashManager;
import com.vulcanth.commons.player.role.Role;
import com.vulcanth.commons.player.role.RoleEnum;
import com.vulcanth.commons.utils.BukkitUtils;
import com.vulcanth.commons.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Delivery {

    MB_MEMBRO("1", RoleEnum.MEMBRO, "ENDER_CHEST : 1 : nome>{color}1 Caixa Misteriosa : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos\n \n &8▪ &f1 Caixa Misteiosa Geral 01\n \n{state}", 30, "mb:1", 11),
    MB_VIP("2", RoleEnum.VIP, "ENDER_CHEST : 5 : nome>{color}5 Caixas Misteriosas : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos\n \n &8▪ &f3 Caixa Misteiosa Geral 01\n &8▪ &f1 Caixa Misteiosa BedWars 01\n &8▪ &f1 Caixa Misteiosa SkyWars 01\n \n{state}", 30, "mb:1", 12),
    MB_MVP("3", RoleEnum.MVP, "ENDER_CHEST : 10 : nome>{color}10 Caixas Misteriosas : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos\n \n &8▪ &f3 Caixa Misteiosa Geral 01\n &8▪ &f4 Caixa Misteiosa BedWars 01\n &8▪ &f3 Caixa Misteiosa SkyWars 01\n \n{state}", 30, "mb:1", 13),
    MB_MVPPLUS("4", RoleEnum.MVPPLUS, "ENDER_CHEST : 15 : nome>{color}15 Caixas Misteriosas : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos\n \n &8▪ &f5 Caixa Misteiosa Geral 01\n &8▪ &f5 Caixa Misteiosa BedWars 01\n &8▪ &f5 Caixa Misteiosa SkyWars 01\n \n{state}", 30, "mb:1", 14),
    MB_YOUTUBER("5", RoleEnum.YOUTUBER, "ENDER_CHEST : 20 : nome>{color}20 Caixas Misteriosas : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos\n \n &8▪ &f12 Caixa Misteiosa Geral 01\n &8▪ &f4 Caixa Misteiosa BedWars 01\n &8▪ &f4 Caixa Misteiosa SkyWars 01\n \n{state}", 30, "mb:1", 15),
    CASH_VIP("6", RoleEnum.VIP, "{CASH_ICON} : 1 : nome>{color}250 Cash : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos!\n \n{state}", 30, "cash:250", 21),
    CASH_MVP("7", RoleEnum.MVP, "{CASH_ICON} : 1 : nome>{color}1000 Cash : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos!\n \n{state}", 30, "cash:1000", 22),
    CASH_MVPPLUS("8", RoleEnum.MVPPLUS, "{CASH_ICON} : 1 : nome>{color}1500 Cash : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos!\n \n{state}", 30, "cash:1500", 23),
    BOOSTER_MVP("9", RoleEnum.MVP, "{BOOSTER_ICON} : 2 : esconder>tudo : nome>{color}2 Multiplicadores de Coins : desc>&7Gratuito para você! Colete mensalmente\n&7para conseguir ainda mais coins.\n \n &8▪ &f2 Multiplicadores Pessoal 2.0x &7(2 horas)\n \n{state}", 30, "booster:p:2", 31),
    BOOSTER_MVPPLUS("10", RoleEnum.MVPPLUS, "{BOOSTER_ICON} : 3 : esconder>tudo : nome>{color}3 Multiplicadores de Coins : desc>&7Gratuito para você! Colete mensalmente\n&7para conseguir ainda mais coins.\n \n &8▪ &f3 Multiplicadores Pessoal 2.0x &7(2 horas)\n \n{state}", 30, "booster:p:3", 32);

    public static List<Delivery> listAllDeliveryNotClaim(Profile profile) {
        return Arrays.stream(Delivery.values()).filter(delivery -> !profile.getCache(PlayerDeliveryCache.class).hasColletedDelivery(delivery.id)).collect(Collectors.toList());
    }

    public static Delivery findByID(String deliveryID) {
        return Arrays.stream(Delivery.values()).filter(delivery -> delivery.getId().equals(deliveryID)).findFirst().orElse(null);
    }

    private final String id;
    private final RoleEnum role;
    private final String icon;
    private final Integer days;
    private final String reward;
    private final int slot;

    Delivery(String id, RoleEnum role, String icon, Integer days, String reward, int slot) {
        this.id = id;
        this.role = role;
        this.icon = icon;
        this.days = days;
        this.reward = reward;
        this.slot = slot;
    }

    public String getIcon() {
        return this.icon;
    }

    public ItemStack getItem(Profile profile) {
        PlayerDeliveryCache cache = profile.getCache(PlayerDeliveryCache.class);
        Player player = profile.getPlayer();

        boolean hasClaim = cache.hasColletedDelivery(this.id);
        return BukkitUtils.getItemStackFromString(this.icon.replace("{color}", hasClaim ? "&c" : "&a").replace("{state}", !canCollect(player) ? "&cExclusivo para " + this.role.getName() + " &cou superior."  : hasClaim ? "&cVocê podera coletar novamente em &f" + StringUtils.transformTimeFormated(cache.getInSecounds(this.id)) : "&eClique para coletar!").replace("{CASH_ICON}", hasClaim ? "328" : "342").replace("{BOOSTER_ICON}", hasClaim ? "374" : "POTION:8261"));
    }

    public boolean canCollect(Player player) {
        return player.hasPermission("deliverys.bypass") || this.role.getId() == Role.findRole(player).getId();
    }

    public void setupReward(Profile profile) {
        String[] rewards = this.reward.split(" ; ");
        for (String reward : rewards) {
            String type = reward.split(":")[0];
            String value = reward.split(":")[1];
            String subValue = "";
            if (reward.split(":").length > 2) {
                subValue = reward.split(":")[2];
            }

            switch (type.toLowerCase()) {
                case "mb": {
                    profile.getPlayer().sendMessage("§aVocê ganhou caixas misteriosas");
                    break;
                }

                case "cash": {
                    new CashManager(profile).addCash(Long.valueOf(value));
                    break;
                }

                case "booster": {
                    profile.getPlayer().sendMessage("§aVocê ganhou o booster do tipo '" + value + "' para " + subValue + "hrs!");
                    break;
                }
            }
        }
    }

    public Integer getDays() {
        return this.days;
    }

    public RoleEnum getRole() {
        return this.role;
    }

    public String getId() {
        return this.id;
    }

    public String getReward() {
        return this.reward;
    }

    public int getSlot() {
        return this.slot;
    }
}
