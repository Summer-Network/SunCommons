package com.vulcanth.commons.storage.redisresponces.collections;

import com.vulcanth.commons.storage.redisresponces.RedisResponceAbstract;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class PlayerRoleUpdater extends RedisResponceAbstract {

    private PlayerRoleUpdater classInstance;

    public PlayerRoleUpdater(PlayerRoleUpdater classInstance) {
        super("playerrole");
        this.classInstance = classInstance;
    }

    @Override
    public void setupAction(String key, DataInputStream value) throws IOException {
        update(key, Long.parseLong(value.readUTF()));
    }

    abstract void update(String name, long roleId);
}
