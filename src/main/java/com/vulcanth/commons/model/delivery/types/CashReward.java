package com.vulcanth.commons.model.delivery.types;

import com.vulcanth.commons.model.delivery.DeliveryReward;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CashReward implements DeliveryReward {

  private double amount;

  @Override
  public void dispath(Player player) {
    //addCash(player, amount);
    //player.sendMessage("§aVocê coletou a recompensa de "
    //    + StringUtils.formatNumber(amount) + " Cash!");
  }
}
