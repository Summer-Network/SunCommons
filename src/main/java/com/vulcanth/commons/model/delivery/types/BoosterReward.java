package com.vulcanth.commons.model.delivery.types;

import com.vulcanth.commons.model.booster.Booster;
import com.vulcanth.commons.model.delivery.DeliveryReward;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class BoosterReward implements DeliveryReward {

  private long hours;
  private double multiplier;
  private Booster.BoosterType type;
  private int amount;

  @Override
  public void dispatch(Player player) {
    //profile.addBooster(new Booster(hours, multiplier, type));
    //player.sendMessage("Mensagem!");
  }
}
