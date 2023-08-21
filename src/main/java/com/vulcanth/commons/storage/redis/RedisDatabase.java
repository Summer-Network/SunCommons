package org.nebula.core.storage.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import redis.clients.jedis.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class RedisDatabase {

  private final String hostname, password;
  private final int port;

  @Getter private JedisPool pool = null;
  private final ExecutorService executor = Executors.newCachedThreadPool();

  public boolean isConnected() {
    return this.pool != null && !this.pool.isClosed();
  }

  public void connect() {
    if (password.isEmpty()) {
      pool = new JedisPool(new JedisPoolConfig(), hostname, port, 0);
    } else {
      pool = new JedisPool(new JedisPoolConfig(), hostname, port, 0, password);
    }
  }

  public void disconnect() {
    if (this.pool != null) this.pool.close();
  }

  public void publish(String channel, String message) {
    Jedis resource = pool.getResource();
    Pipeline pipeline = resource.pipelined();
    pipeline.publish(channel, message);
    pipeline.sync();
  }

  public void subscribe(JedisPubSub pubSub, String... channels) {
    executor.submit(() -> {
      Jedis resource = pool.getResource();
      resource.subscribe(pubSub, channels);
    });
  }
}