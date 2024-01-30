package com.summer.commons.storage;

import com.summer.commons.Main;
import com.summer.commons.bungee.BungeeMain;
import com.summer.commons.storage.types.MySQL;
import com.summer.commons.storage.types.Redis;

public class Database {

  private static MySQL mySQL = null;
  private static Redis redis = null;

  public static void setupDatabase(boolean bungee) {
    mySQL = new MySQL("142.44.135.166", "3306", "s2941_sun", "u2941_kQvoPpQVXM", "OAm+@zDwcp1I73orkMdfjHcy", bungee);
    redis = new Redis("penesgrandenoiado", "144.217.153.90", "7777");

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
