package com.vulcanth.commons.model.booster;

import com.vulcanth.commons.Main;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public class Booster {

  private long hours;
  private double multiplier;
  private BoosterType type;

  public void active(Player player) {
    switch (type) {
      case PRIVATE:
        //Profile.setBooster
        break;
      case NETWORK:
        //Main.minigame

        new NetworkBooster(
            player.getName(), multiplier, TimeUnit.HOURS.toMillis(hours));
        break;
      case CLAN:
        //Clan.getByMember(player.getName());
        break;
    }
  }

  enum BoosterType {
    PRIVATE, CLAN, NETWORK
  }
}
