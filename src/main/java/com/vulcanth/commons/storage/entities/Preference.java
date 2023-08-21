package org.nebula.core.storage.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Preference {

  private String name;
  private int ordinal;

  public <T extends Enum<T>> T toType(Class<T> enumClass) {
    T[] enumConstants = enumClass.getEnumConstants();
    if (ordinal >= 0 && ordinal < enumConstants.length) {
      return enumConstants[ordinal];
    }
    return null;
  }
}
