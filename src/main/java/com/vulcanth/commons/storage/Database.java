package com.vulcanth.commons.storage;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.storage.types.MySQL;
import com.vulcanth.commons.storage.types.Redis;

public class Database {

  private static MySQL mySQL = null;
  private static Redis redis = null;

  public static void setupDatabase(boolean bungee) {
    mySQL = new MySQL("149.56.38.55", "3306", "s2_core", "u2_97on0bXdg2", "=!LQj!OmDoRThwQ8bkk=Tvqn", bungee);
    redis = new Redis("VulcanthInTheTop", "ca-01.antryhost.com", "25570", bungee);

    mySQL.setupTables();

    if (bungee) {
      BungeeMain.getInstance().sendMessage("Todos os database foram conectados com sucesso!");
    } else {
      Main.getInstance().sendMessage("Todos os database foram conectados com sucesso!");
    }
  }

  public static void disconnect() {
    if (mySQL != null) {
      mySQL.destroy();
    }

    if (redis != null) {
      redis.destroy();
    }
  }

  public static MySQL getMySQL() {
    return mySQL;
  }

  public static Redis getRedis() {
    return redis;
  }
}
