package com.vulcanth.commons.delivery;

import com.vulcanth.commons.delivery.types.CoinsReward;
import com.vulcanth.commons.delivery.types.MysteryBoxReward;
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

@Getter
@AllArgsConstructor
public class Delivery {

  private long id;
  private int days;
  private int slot;
  private String permission;
  private List<DeliveryReward> rewards;
  private ItemStack icon;

  public static final List<Delivery> DELIVERIES = new ArrayList<>();

  public static void makeDeliveries() {
    DELIVERIES.add(new Delivery(1, 30,  10, "",
        Collections.singletonList(new MysteryBoxReward("5 stars", 1)),
        ItemBuilder
            .of(Material.ENDER_CHEST)
            .display("1 Caixa Misteriosa")
            .lore("§7lore!")
            .build()
    ));

    DELIVERIES.add(new Delivery(2, 30,  11, "",
        Collections.singletonList(new MysteryBoxReward("5 stars", 3)),
        ItemBuilder
            .of(Material.ENDER_CHEST)
            .display("1 Caixa Misteriosa")
            .lore("§7lore!")
            .build()
    ));
  }
}
