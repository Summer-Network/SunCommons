package org.nebula.core.storage.sql;

import org.nebula.core.storage.Database;

import java.util.ArrayList;
import java.util.List;

public class Migrations {

  private static final List<String> QUERIES = new ArrayList<>();

  static {
    QUERIES.add("CREATE TABLE IF NOT EXISTS `cm_profile` (`name` VARCHAR(32), `cash` DOUBLE, `last_login` LONG, `first_login` LONG, `preferences` LONGTEXT, PRIMARY KEY(`name`));");

    //QUERIES.add("CREATE TABLE IF NOT EXISTS `cm_profile` (" +
    //        "`name` VARCHAR(32), `cash` LONG, `role` TEXT, `tags` TEXT, `levels` TEXT," +
    //        "`deliveries` TEXT, `preferences` TEXT, `boosters` TEXT," +
    //        "`firstlogin` LONG, `lastlogin` LONG, `lastlogout` LONG, PRIMARY KEY(`name`));");
  }

  public static void migrate() {
    QUERIES.forEach(query -> Database.getProvider().update(query));
  }

  public static void register(String query) {
    QUERIES.add(query);
  }
}
