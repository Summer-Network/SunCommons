package com.vulcanth.commons.model.delivery.types;

import com.vulcanth.commons.model.delivery.DeliveryReward;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class BoosterReward implements DeliveryReward {

  @Override
  public void dispatch(Player player) {
      // adicionar booster
  }
}
