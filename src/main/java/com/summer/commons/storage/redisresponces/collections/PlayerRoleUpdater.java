package com.summer.commons.storage.redisresponces.collections;

import com.summer.commons.storage.redisresponces.RedisResponseAbstract;

import java.io.DataInputStream;
import java.io.IOException;

public class PlayerRoleUpdater extends RedisResponseAbstract {

    private static Updater classInstance = null;

    public PlayerRoleUpdater() {
        super("playerrole");
    }

    @Override
    public void setupAction(String key, DataInputStream value) throws IOException {
        if (classInstance != null) {
            classInstance.update(key, Long.parseLong(value.readUTF()));
        }
    }

    public static void setClassUpdater(Updater updater) {
        classInstance = updater;
    }
}
