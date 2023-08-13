package com.vulcanth.commons.delivery.types;

import com.vulcanth.commons.delivery.DeliveryReward;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class MysteryBoxReward implements DeliveryReward {

  private String type;
  private int amount;

  @Override
  public void dispath(Player player) {
      //addBox(player, type, amount);
  }
}
