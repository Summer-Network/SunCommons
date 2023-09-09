package com.vulcanth.commons.storage.types;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.storage.tables.collections.ProfileTable;
import com.vulcanth.commons.storage.tables.collections.SkinTable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.dropwizard.CodaHaleMetricsTracker;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQL {

    private HikariDataSource resource;
    private Connection connection;

    public MySQL(String host, String port, String databaseName, String username, String password, boolean isBungee) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + databaseName);
            config.setUsername(username);
            config.setPassword(password);
            config.setMinimumIdle(20);
            config.setMaximumPoolSize(100);
            config.setConnectionTimeout(20000);
            config.setIdleTimeout(1000);
            config.setValidationTimeout(3000);
            config.setAutoCommit(true);
            config.setMaxLifetime(1800000);
            this.resource = new HikariDataSource(config);
        } catch (Exception e) {
            if (isBungee) {
                BungeeMain.getInstance().sendMessage("Algo deu errado enquanto conectavamos ao database...", '4');
                BungeeMain.getInstance().getProxy().stop();
            } else {
                Main.getInstance().sendMessage("Algo deu errado enquanto conectavamos ao database...", '4');
                Bukkit.shutdown();
            }
        }
    }

    public void setupTables() {
        new ProfileTable();
        new SkinTable();
    }

    public void insert(PreparedStatement preparedStatement) {
        try {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean conteins(String table, String colunm, Object value) {
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            statement = openConnection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + table);
            while (resultSet.next()) {
                if (resultSet.getString(colunm).equals(value)) {
                    statement.close();
                    resultSet.close();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Problema na DB");
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public List<Object> listAllValues(String table, String column) {
        Statement statement = null;
        ResultSet rs = null;
        List<Object> accountsList = new ArrayList<>();
        try {
            statement = openConnection().createStatement();
            rs = statement.executeQuery("SELECT * FROM " + table);
            while (rs.next()) {
                accountsList.add(rs.getObject(column));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return accountsList;
    }

    public Object getValue(String table, String column, String columnParameter, String logic, String logicValue) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = openConnection().createStatement();
            rs = statement.executeQuery("SELECT * FROM " + table + " WHERE " + columnParameter +  " " + logic + " '" + logicValue + "'");
            Object result = null;
            while (rs.next()) {
                result = rs.getObject(column);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void update(String table, String column, String logic, String columnParamenter, String logicValue, Object newValue) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = openConnection().prepareStatement("UPDATE " + table + " SET " + column + " = '" + newValue + "' WHERE " + columnParamenter + " " + logic + " '" + logicValue + "'");
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy() {
        this.resource.close();
        this.resource = null;
        System.out.println("MySQL desligado com sucesso!");
    }

    public Connection openConnection() {
        try {
            if(connection == null || connection.isClosed()){
                this.connection = resource.getConnection();
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HikariDataSource getConnection() {
        return this.resource;
    }

    public boolean isConnected() {
        return this.resource != null && this.resource.isRunning();
    }
}
