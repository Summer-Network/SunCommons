package com.vulcanth.commons.model.delivery.types;

import com.vulcanth.commons.model.delivery.DeliveryReward;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CoinsReward implements DeliveryReward {

  private String minigame;
  private double amount;

  @Override
  public void dispath(Player player) {
    //addCoins(player, minigame, amount);
    //player.sendMessage("§aVocê coletou a recompensa de "
    //    + StringUtils.formatNumber(amount) + " Coins!");
  }
}
