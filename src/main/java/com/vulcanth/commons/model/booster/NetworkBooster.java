package com.vulcanth.commons.model.booster;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NetworkBooster {

  private String owner;
  private double multiplier;
  private long expires;
}
