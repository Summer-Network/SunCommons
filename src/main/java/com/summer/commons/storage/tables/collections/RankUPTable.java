package com.summer.commons.storage.tables.collections;

import com.summer.commons.storage.Database;
import com.summer.commons.storage.tables.TableAbstract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class RankUPTable extends TableAbstract {

    public static void setDefault(String name) {
        Connection connection = Database.getMySQL().openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO SunRank VALUES(?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, "{}");
            ps.setString(3, "{}");
            ps.setLong(4, 0L);
            Database.getMySQL().insert(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(String name, String column, Object value) {
        Database.getMySQL().update("SunRank", column, "=", "NAME", name, value);
    }

    public static Object getInformation(String name, String column) {
        return Database.getMySQL().getValue("SunRank", column, "NAME", "=", name);
    }

    @Override
    public void setupTable() {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS SunRank(`NAME` TEXT NOT NULL, `CLAN` TEXT, `MONEY` LONG, `INFORMATIONS` TEXT) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
