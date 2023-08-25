package com.vulcanth.commons.storage.tables.collections;

import com.vulcanth.commons.storage.Database;
import com.vulcanth.commons.storage.tables.TableAbstract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfileTable extends TableAbstract {

    public static void setDefault(String name) {
        Connection connection = Database.getMySQL().openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO VulcanthProfiles values(?,?,?,?)");
            ps.setString(1, name);
            ps.setString(2, "{}");
            ps.setString(3, "{}");
            ps.setString(4, "{}");
            Database.getMySQL().insert(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(String name, String column, Object value) {
        Database.getMySQL().update("VulcanthProfiles", column, "=", "NAME", name, value);
    }

    public static Object getInformation(String name, String column) {
        return Database.getMySQL().getValue("VulcanthProfiles", column, "NAME", "=", name);
    }

    @Override
    public void setupTable() {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS VulcanthProfiles(`NAME` TEXT NOT NULL, `INFORMATIONS` TEXT, `PREFERENCES` TEXT, `REWARDS` TEXT) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
