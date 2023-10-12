package com.vulcanth.commons.storage.tables.collections;

import com.vulcanth.commons.storage.Database;
import com.vulcanth.commons.storage.tables.TableAbstract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginTable extends TableAbstract {

    public static void setDefault(String name) {
        Connection connection = Database.getMySQL().openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO VulcanthLogin VALUES(?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, "{}");
            ps.setString(3, "{}");
            Database.getMySQL().insert(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(String name, String column, Object value) {
        Database.getMySQL().update("VulcanthLogin", column, "=", "NAME", name, value);
    }

    public static Object getInformation(String name, String column) {
        return Database.getMySQL().getValue("VulcanthLogin", column, "NAME", "=", name);
    }

    @Override
    public void setupTable() {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS VulcanthLogin(`NAME` TEXT NOT NULL, `PASSWORD` TEXT, `ADDRESS` TEXT) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
