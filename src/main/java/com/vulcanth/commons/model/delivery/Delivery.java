package com.vulcanth.commons.model.delivery;

import com.vulcanth.commons.model.booster.Booster;
import com.vulcanth.commons.model.delivery.types.BoosterReward;
import com.vulcanth.commons.model.delivery.types.CashReward;
import com.vulcanth.commons.model.delivery.types.MysteryBoxReward;
import com.vulcanth.commons.utils.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public class Delivery {

  private long id;
  private int days;
  private int slot;
  private String permission;
  private List<DeliveryReward> rewards;
  private ItemStack icon;

  public void collect(Player player) {
    //setCollected(id, player, System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days));
    rewards.forEach(reward -> reward.dispatch(player));
  }

  public boolean hasPermission(Player player) {
    return permission.isEmpty() || player.hasPermission(permission);
  }

  public static final List<Delivery> DELIVERIES = new ArrayList<>();

  public static void makeDeliveries() {
    DELIVERIES.add(new Delivery(DELIVERIES.size(), 30,  11, "",
        Collections.singletonList(new MysteryBoxReward("hybrid", 1)),
        ItemBuilder
            .of(Material.ENDER_CHEST)
            .display("1 Caixa Misteriosa")
            .lore("§7Gratuito para você! Colete mensalmente",
                "§7e adquira itens épicos!",
                "",
                "§f* 01 §bCaixas Misteriosas Híbridas"
            ).build()
    ));

    int slot = 12;
    int[] amount = new int[]{5, 12, 16, 20};
    String[] permissions = new String[]{"role.vip", "role.mvp", "role.mvpplus", "role.youtuber"};
    for (int i = 0; i < 4; i++) {
      int hybrid = amount[i] / 2;
      int minigame = hybrid / 2;

      DELIVERIES.add(new Delivery(DELIVERIES.size(), 30,  slot++, permissions[i],
          Arrays.asList(
              new MysteryBoxReward("hybrid", hybrid),
              new MysteryBoxReward("sky wars", minigame),
              new MysteryBoxReward("bed wars", minigame)
          ),
          ItemBuilder
              .of(Material.ENDER_CHEST)
              .display(amount[i] + " Caixas Misteriosas")
              .lore("§7Gratuito para você! Colete mensalmente",
                  "§7e adquira itens épicos!",
                  "",
                  "§f* " + hybrid + " §bCaixas Misteriosas Híbridas",
                  "§f* " + minigame + " §bCaixas Misteriosas Sky Wars",
                  "§f* " + minigame + " §bCaixas Misteriosas Bed Wars"
              ).build()
      ));
    }

    slot = 21;
    amount = new int[]{250, 1000, 1500};
    permissions = new String[]{"role.vip", "role.mvp", "role.mvpplus"};
    for (int i = 0; i < 3; i++) {
      DELIVERIES.add(new Delivery(DELIVERIES.size(), 30, slot++, permissions[i],
          Collections.singletonList(new CashReward(amount[i])),
          ItemBuilder
              .of(Material.STORAGE_MINECART)
              .display(amount[i] + " Cash")
              .lore("§7Gratuito para você! Colete mensalmente", "§7e adquira itens épicos!")
              .build()
      ));
    }

    slot = 31;
    amount = new int[]{3, 5};
    permissions = new String[]{"role.mvp", "role.mvpplus"};
    for (int i = 0; i < 2; i++) {
      DELIVERIES.add(new Delivery(DELIVERIES.size(), 30, slot++, permissions[i],
          Collections.singletonList(
              new BoosterReward(1, 2, Booster.BoosterType.NETWORK, amount[i])),
          ItemBuilder
              .of(Material.POTION).durability((short) 0)
              .display(amount[i] + " Boosters de Coins")
              .lore("§7Gratuito para você! Colete mensalmente",
                  "§7para conseguir ainda mais coins.",
                  "",
                  "§fTipo: §7Geral",
                  "§fMultiplicador: §7Geral",
                  "§fDuração: §71h"
              ).build()
      ));
    }
  }

  public static Delivery getBySlot(int slot) {
    return DELIVERIES.stream().filter(d -> d.getSlot() == slot).findFirst().orElse(null);
  }
}
