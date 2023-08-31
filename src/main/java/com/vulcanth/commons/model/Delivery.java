package com.vulcanth.commons.model;

import com.vulcanth.commons.player.role.RoleEnum;

public enum Delivery {

    MB_MEMBRO("1", RoleEnum.MEMBRO, "ENDER_CHEST : 1 : nome>{color}1 Caixa Misteriosa : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos\n \n &8▪ &f1 Caixa Misteiosa Geral 01\n \n{state}", 30, "mb:1"),
    MB_VIP("1", RoleEnum.VIP, "ENDER_CHEST : 5 : nome>{color}5 Caixas Misteriosas : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos\n \n &8▪ &f3 Caixa Misteiosa Geral 01\n &8▪ &f1 Caixa Misteiosa BedWars 01\n &8▪ &f1 Caixa Misteiosa SkyWars 01\n \n{state}", 30, "mb:1"),
    MB_MVP("1", RoleEnum.MVP, "ENDER_CHEST : 10 : nome>{color}10 Caixas Misteriosas : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos\n \n &8▪ &f3 Caixa Misteiosa Geral 01\n &8▪ &f4 Caixa Misteiosa BedWars 01\n &8▪ &f3 Caixa Misteiosa SkyWars 01\n \n{state}", 30, "mb:1"),
    MB_MVPPLUS("1", RoleEnum.MVPPLUS, "ENDER_CHEST : 15 : nome>{color}15 Caixas Misteriosas : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos\n \n &8▪ &f5 Caixa Misteiosa Geral 01\n &8▪ &f5 Caixa Misteiosa BedWars 01\n &8▪ &f5 Caixa Misteiosa SkyWars 01\n \n{state}", 30, "mb:1"),
    MB_YOUTUBER("1", RoleEnum.YOUTUBER, "ENDER_CHEST : 20 : nome>{color}20 Caixas Misteriosas : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos\n \n &8▪ &f12 Caixa Misteiosa Geral 01\n &8▪ &f4 Caixa Misteiosa BedWars 01\n &8▪ &f4 Caixa Misteiosa SkyWars 01\n \n{state}", 30, "mb:1"),
    CASH_VIP("1", RoleEnum.VIP, "{CASH_ICON} : 1 : nome>{color}250 Cash : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos!\n \n{state}", 30, "cash:250"),
    CASH_MVP("1", RoleEnum.MVP, "{CASH_ICON} : 1 : nome>{color}1000 Cash : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos!\n \n{state}", 30, "cash:1000"),
    CASH_MVPPLUS("1", RoleEnum.MVPPLUS, "{CASH_ICON} : 1 : nome>{color}1500 Cash : desc>&7Gratuito para você! Colete mensalmente\n&7e adquira itens épicos!\n \n{state}", 30, "cash:1500"),
    BOOSTER_MVP("1", RoleEnum.MVP, "{BOOSTER_ICON} : 2 : esconder>tudo : nome>{color}2 Multiplicadores de Coins : desc>&7Gratuito para você! Colete mensalmente\n&7para conseguir ainda mais coins.\n \n &8▪ &f2 Multiplicadores Pessoal 2.0x &7(2 horas)\n \n{state}", 30, "booster:p:2"),
    BOOSTER_MVPPLUS("1", RoleEnum.MVPPLUS, "{BOOSTER_ICON} : 3 : esconder>tudo : nome>{color}3 Multiplicadores de Coins : desc>&7Gratuito para você! Colete mensalmente\n&7para conseguir ainda mais coins.\n \n &8▪ &f3 Multiplicadores Pessoal 2.0x &7(2 horas)\n \n{state}", 30, "booster:p:3");

    private final String id;
    private final RoleEnum role;
    private final String icon;
    private final Integer days;
    private final String reward;

    Delivery(String id, RoleEnum role, String icon, Integer days, String reward) {
        this.id = id;
        this.role = role;
        this.icon = icon;
        this.days = days;
        this.reward = reward;
    }

    public String getIcon() {
        return this.icon;
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
}
