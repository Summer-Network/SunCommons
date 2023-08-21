package org.nebula.core.storage.providers;

import lombok.RequiredArgsConstructor;
import org.nebula.core.bukkit.Main;
import org.nebula.core.storage.interfaces.ISQLProvider;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class MySQLProvider implements ISQLProvider {

  private final String host, port, name, username, password;

  private Connection connection = null;
  private final ExecutorService executor = Executors.newCachedThreadPool();

  public void connect() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name + "?verifyServerCertificate=false&useSSL=false&useUnicode=yes&characterEncoding=UTF-8", username, password);

      Main.getInstance().getLogger().info("Conectado ao MySQL!");
    } catch (Exception ex) {
      System.exit(0);
    }
  }

  public void disconnect() {
    if (isConnected()) {
      try {
        connection.close();
      } catch (SQLException ignored) {}
    }
  }

  public Connection getConnection() throws SQLException {
    if (!isConnected()) connect();

    return connection;
  }

  public boolean isConnected() {
    try {
      return !(connection == null || connection.isClosed() || !connection.isValid(5));
    } catch (SQLException ex) {
      return false;
    }
  }

  public void update(String sql, Object... vars) {
    try (PreparedStatement ps = prepareStatement(sql, vars)) {
      ps.executeUpdate();
    } catch (SQLException ignored) {}
  }

  public void execute(String sql, Object... vars) {
    executor.execute(() -> {
      update(sql, vars);
    });
  }

  public int updateReturnId(String sql, Object... vars) {
    int id = -1;
    ResultSet rs = null;
    try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      for (int i = 0; i < vars.length; i++) {
        ps.setObject(i + 1, vars[i]);
      }
      ps.execute();
      rs = ps.getGeneratedKeys();
      if (rs.next()) {
        id = rs.getInt(1);
      }
    } catch (SQLException ignored) {} finally {
      try {
        if (rs != null && !rs.isClosed())
          rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return id;
  }

  public PreparedStatement prepareStatement(String query, Object... vars) {
    try {
      PreparedStatement ps = getConnection().prepareStatement(query);
      for (int i = 0; i < vars.length; i++) {
        ps.setObject(i + 1, vars[i]);
      }
      return ps;
    } catch (SQLException ignored) {}

    return null;
  }

  public CachedRowSet query(String query, Object... vars) {
    CachedRowSet rowSet = null;
    try {
      Future<CachedRowSet> future = executor.submit(() -> {
        CachedRowSet crs = null;
        try (PreparedStatement ps = prepareStatement(query, vars); ResultSet rs = ps.executeQuery()) {

          CachedRowSet rs2 = RowSetProvider.newFactory().createCachedRowSet();
          rs2.populate(rs);

          if (rs2.next()) {
            crs = rs2;
          }
        } catch (SQLException ignored) {}

        return crs;
      });

      if (future.get() != null) {
        rowSet = future.get();
      }
    } catch (Exception ignored) {}

    return rowSet;
  }
}
