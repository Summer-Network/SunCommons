package com.vulcanth.commons.model.booster;

import com.vulcanth.commons.Main;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public class Booster {

  private long hours;
  private double multiplier;
  private BoosterType type;

  public long makeExpires() {
    return System.currentTimeMillis() + TimeUnit.HOURS.toMillis(hours);
  }

  public static final Map<String, NetworkBooster> NETWORK_BOOSTERS = new HashMap<>();

  public void active(Player player) {
    switch (type) {
      case PRIVATE:
        //profile.setBooster(multiplier, makeExpires());
        break;
      case NETWORK:
        NetworkBooster booster = NETWORK_BOOSTERS.get(Main.minigame);

        if (booster != null && booster.getExpires() > System.currentTimeMillis()) return;

        booster = new NetworkBooster(player.getName(), multiplier, makeExpires());

        //RedisBackend.getInstance().publishData("booster", Main.minigame,
        //    booster.getOwner(), booster.getMultiplier(), booster.getExpires()
        //);

        NETWORK_BOOSTERS.put(Main.minigame, booster);
        break;
      case CLAN:
        //Clan clan = Clan.getByMember(player.getName());
        //if (!clan.hasBooster()) {
        //  clan.setBooster(multiplier, getExpires());
        //}
        break;
    }
  }

  enum BoosterType {
    PRIVATE, CLAN, NETWORK
  }
}
