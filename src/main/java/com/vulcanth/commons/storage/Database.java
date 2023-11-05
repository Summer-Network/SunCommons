package com.vulcanth.commons.storage;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.storage.types.MySQL;
import com.vulcanth.commons.storage.types.Redis;

public class Database {

  private static MySQL mySQL = null;
  private static Redis redis = null;

  public static void setupDatabase(boolean bungee) {
    mySQL = new MySQL("135.148.29.98", "3306", "s6_vulcanth", "u6_JJnvqiEAuO", "1KGCTvBqIWUHL@.kHQ7zcd1P", bungee);
    redis = new Redis("VulcanthInTheTop", "i7.vulcanth.com", "25569");

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
