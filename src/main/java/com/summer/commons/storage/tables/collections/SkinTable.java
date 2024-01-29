package com.summer.commons.storage.tables.collections;

import com.summer.commons.storage.Database;
import com.summer.commons.storage.tables.TableAbstract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SkinTable extends TableAbstract {

    public static void setDefault(String name) {
        Connection connection = Database.getMySQL().openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO VulcanthSkins VALUES(?, ?)");
            ps.setString(1, name);
            ps.setString(2, "{}");
            Database.getMySQL().insert(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(String name, String column, Object value) {
        Database.getMySQL().update("VulcanthSkins", column, "=", "NAME", name, value);
    }

    public static Object getInformation(String name, String column) {
        return Database.getMySQL().getValue("VulcanthSkins", column, "NAME", "=", name);
    }

    @Override
    public void setupTable() {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS VulcanthSkins(`NAME` TEXT NOT NULL, `SKININFO` TEXT) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
