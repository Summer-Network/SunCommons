package com.vulcanth.commons.storage;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.storage.types.MySQL;
import com.vulcanth.commons.storage.types.Redis;

public class Database {

  private static MySQL mySQL = null;
  private static Redis redis = null;

  public static void setupDatabase() {
    mySQL = new MySQL(Main.getInstance().getConfig().getString("host"), Main.getInstance().getConfig().getString("port"), Main.getInstance().getConfig().getString("database"), Main.getInstance().getConfig().getString("user"), Main.getInstance().getConfig().getString("password"));
    //redis = new Redis("", "", "");

    mySQL.setupTables();

    Main.getInstance().sendMessage("Todos os database foram conectados com sucesso!");
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
