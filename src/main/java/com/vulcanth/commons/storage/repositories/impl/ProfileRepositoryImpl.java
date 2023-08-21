package org.nebula.core.storage.repositories.impl;

import org.nebula.core.storage.Database;
import org.nebula.core.storage.entities.Preference;
import org.nebula.core.storage.entities.Profile;
import org.nebula.core.storage.mappers.PreferenceMapper;
import org.nebula.core.storage.repositories.ProfileRepository;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.List;

public class ProfileRepositoryImpl implements ProfileRepository {

  public static ProfileRepository instance = new ProfileRepositoryImpl();

  private static final String TABLE = "cm_profile";

  @Override
  public Profile getProfile(String name) {
    String query = "SELECT * FROM `" + TABLE + "` WHERE `name` = ?";

    try (CachedRowSet rs = Database.getProvider().query(query, name)) {
      if (rs != null) {
        double cash = rs.getDouble("cash");
        long lastLogin = rs.getLong("last_login");
        long firstLogin = rs.getLong("first_login");
        List<Preference> preferences = PreferenceMapper.toDomain(rs.getString("preferences"));

        return new Profile(name, cash, lastLogin, firstLogin, preferences);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    this.insertProfile(name);

    return getProfile(name);
  }

  @Override
  public void insertProfile(String name) {
    String query = "INSERT INTO `" + TABLE + "` VALUES (?, ?, ?, ?, ?)";
    Database.getProvider().execute(query, name, 0, System.currentTimeMillis(), System.currentTimeMillis(), "[]");
  }

  @Override
  public void updateProfile(Profile entity) {
    String preferencePersistence = PreferenceMapper.toPersistence(entity.getPreferences());

    String query = "UPDATE `" + TABLE + "` SET `cash` = ?, `last_login` = ?, `first_login` = ?, `preferences` = ? WHERE `name` = ?";
    Database.getProvider().update(query, entity.getCash(), entity.getLastLogin(), entity.getFirstLogin(), preferencePersistence, entity.getName());
  }
}
