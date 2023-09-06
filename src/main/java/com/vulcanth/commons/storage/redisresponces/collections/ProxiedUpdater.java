package com.vulcanth.commons.storage.redisresponces.collections;

import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.storage.redisresponces.RedisResponceAbstract;

import java.io.DataInputStream;
import java.io.IOException;

public class ProxiedUpdater extends RedisResponceAbstract {

    public ProxiedUpdater() {
        super("proxiedprofile");
    }

    @Override
    public void setupAction(String key, DataInputStream value) throws IOException {
        BungeeMain.getInstance().sendMessage(key);
        BungeeMain.getInstance().sendMessage(value.readUTF());
    }
}
