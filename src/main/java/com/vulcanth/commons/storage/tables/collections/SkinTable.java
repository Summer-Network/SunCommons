package com.vulcanth.commons.storage.tables.collections;

import com.vulcanth.commons.storage.Database;
import com.vulcanth.commons.storage.tables.TableAbstract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SkinTable extends TableAbstract {

    public static void setDefault(String name) {
        Connection connection = Database.getMySQL().openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO VulcanthSkins values(?,?,?,?)");
            ps.setString(1, name);
            ps.setString(2, "{}");
            ps.setString(3, "{}");
            ps.setString(4, "{}");
            Database.getMySQL().insert(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupTable() {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS VulcanthSkins(`NAME` TEXT NOT NULL, `VALUE` TEXT, `SIGNATURE` TEXT, `SKINNAME` TEXT) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
