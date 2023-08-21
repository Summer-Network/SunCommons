package org.nebula.core.storage;

import lombok.Getter;
import org.nebula.core.storage.interfaces.ISQLProvider;
import org.nebula.core.storage.providers.MySQLProvider;
import org.nebula.core.storage.redis.RedisDatabase;
import org.nebula.core.storage.sql.Migrations;

public class Database {

  @Getter private static RedisDatabase redis;
  @Getter private static ISQLProvider provider;

  public static void setupDatabase() {
    redis = new RedisDatabase("localhost", "", 6379);
    redis.connect();

    provider = new MySQLProvider(
        "localhost",
        "3306",
        "test",
        "root",
        "");
    provider.connect();

    Migrations.migrate();
  }

  public static void shutdown() {
    redis.disconnect();
    provider.disconnect();
  }
}
