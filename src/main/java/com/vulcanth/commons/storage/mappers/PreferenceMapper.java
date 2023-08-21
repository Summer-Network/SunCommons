package org.nebula.core.storage.mappers;

import com.google.gson.Gson;
import org.nebula.core.storage.entities.Preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreferenceMapper {

  public static List<Preference> toDomain(String json) {
    Preference[] preferencesArray = new Gson().fromJson(json, Preference[].class);
    return new ArrayList<>(Arrays.asList(preferencesArray));
  }

  public static String toPersistence(List<Preference> preferences) {
    return new Gson().toJson(preferences);
  }
}