package com.vulcanth.commons.player.preferences;

import java.util.Arrays;
import java.util.Objects;

public enum PreferencesEnum {

    SHOW_PLAYERS(1),
    PRIVATE_MESSAGES(2),
    BLOOD(3),
    LOBBY_PROTECTION(4);

    public static PreferencesEnum findByID(String id) {
        return Arrays.stream(PreferencesEnum.values()).filter(preferencesEnum -> Objects.equals(preferencesEnum.getId(), id)).findFirst().orElse(null);
    }

    private final int id;

    PreferencesEnum(int id) {
        this.id = id;
    }

    public String getId() {
        return String.valueOf(this.id);
    }
}
