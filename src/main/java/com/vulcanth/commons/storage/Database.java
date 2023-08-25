package com.vulcanth.commons.storage;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.storage.types.MySQL;
import com.vulcanth.commons.storage.types.Redis;

public class Database {

  private static MySQL mySQL = null;
  private static Redis redis = null;

  public static void setupDatabase() {
    mySQL = new MySQL("localhost", "3306", "local", "root", "");
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
