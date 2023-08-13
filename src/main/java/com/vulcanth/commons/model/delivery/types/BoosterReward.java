package com.vulcanth.commons.delivery.types;

import com.vulcanth.commons.delivery.DeliveryReward;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class BoosterReward implements DeliveryReward {

  @Override
  public void dispath(Player player) {
      // adicionar booster
  }
}
