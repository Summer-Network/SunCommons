package com.vulcanth.commons.player.preferences;

import java.util.Arrays;

public enum PreferencesEnum {

    SHOW_PLAYERS(1);

    public static PreferencesEnum findByID(int id) {
        return Arrays.stream(PreferencesEnum.values()).filter(preferencesEnum -> preferencesEnum.getId() == id).findFirst().orElse(null);
    }

    private final int id;

    PreferencesEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
