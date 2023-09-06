package com.vulcanth.commons.storage.redisresponces.collections;

import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import com.vulcanth.commons.bungee.proxied.cache.CacheAbstract;
import com.vulcanth.commons.storage.redisresponces.RedisResponceAbstract;

import java.io.DataInputStream;
import java.io.IOException;

public class ProxiedUpdater extends RedisResponceAbstract {

    public ProxiedUpdater() {
        super("proxiedprofile");
    }

    @Override
    public void setupAction(String key, DataInputStream value) throws IOException {
        ProxiedProfile profile = ProxiedProfile.loadProfile(key);
        CacheAbstract cacheAbstract = profile.getCache(value.readUTF());
        if (cacheAbstract != null) {
            cacheAbstract.setValueCache(value.readUTF());
        }
    }
}
