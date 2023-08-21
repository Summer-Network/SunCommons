package org.nebula.core.storage.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Profile {

  private String name;
  private double cash;
  private long lastLogin;
  private long firstLogin;
  private List<Preference> preferences;

  public <T extends Enum<T>> T getPreference(String name, Class<T> enumClass) {
    return getPreference(name).toType(enumClass);
  }

  public Preference getPreference(String name) {
    Preference preference = preferences.stream().filter(p ->
        p.getName().equals(name)
    ).findFirst().orElse(null);

    if (preference == null) {
      preference = new Preference(name, 0);
      preferences.add(preference);
    }
    return preference;
  }
}