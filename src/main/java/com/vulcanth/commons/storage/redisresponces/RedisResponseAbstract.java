package com.vulcanth.commons.storage.redisresponces;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class RedisResponseAbstract {

    private final String channel;

    public RedisResponseAbstract(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return this.channel;
    }

    public abstract void setupAction(String key, DataInputStream value) throws IOException;
}
