package com.vulcanth.commons.model.delivery.types;

import com.vulcanth.commons.model.delivery.DeliveryReward;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class MysteryBoxReward implements DeliveryReward {

  private String type;
  private int amount;

  @Override
  public void dispatch(Player player) {
      //addBox(player, type, amount);
  }
}
