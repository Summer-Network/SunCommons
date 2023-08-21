package org.nebula.core.storage.repositories;

import org.nebula.core.storage.entities.Profile;

public interface ProfileRepository {

  Profile getProfile(String name);

  void insertProfile(String name);

  void updateProfile(Profile entity);
}
