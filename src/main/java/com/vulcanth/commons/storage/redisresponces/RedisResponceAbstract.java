package com.vulcanth.commons.storage.redisresponces;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class RedisResponceAbstract {

    private final String channel;

    public RedisResponceAbstract(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return this.channel;
    }

    public abstract void setupAction(String key, DataInputStream value) throws IOException;
}
