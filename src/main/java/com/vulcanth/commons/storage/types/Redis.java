package com.vulcanth.commons.storage.types;

import com.google.common.io.ByteArrayDataOutput;
import com.vulcanth.commons.Main;
import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.storage.redisresponces.RedisResponceAbstract;
import com.vulcanth.commons.storage.redisresponces.collections.ProxiedUpdater;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Redis {

    private JedisPool resource;
    private String password;
    private List<RedisResponceAbstract> responces;
    private Jedis connection;
    private final ExecutorService executorService;

    public Redis(String password, String host, String port, boolean isBungee) {
        this.password = password;
        this.responces = new ArrayList<>();
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(30);
            config.setMinIdle(5);
            config.setMaxIdle(10);
            config.setMaxWait(3000);
            this.resource = new JedisPool(config, host, Integer.parseInt(port));
            openConnection();
        } catch (Exception e) {
            if (isBungee) {
                BungeeMain.getInstance().sendMessage("Algo deu errado enquanto conectavamos ao redis...", '4');
                BungeeMain.getInstance().getProxy().stop();
            } else {
                Main.getInstance().sendMessage("Algo deu errado enquanto conectavamos ao redis...", '4');
                Bukkit.shutdown();
            }
        }

        try {
            loadResponses(ProxiedUpdater.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setupChannel();
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void openConnection() {
        try {
            this.connection = this.resource.getResource();
            this.connection.auth(this.password);
            this.connection.getClient().setTimeout(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Jedis createConnection() {
        Jedis jedis = this.resource.getResource();
        jedis.auth(this.password);
        jedis.getClient().setTimeout(5000);

        return jedis;
    }

    public void closeConnection() {
        this.resource.destroy();
        this.resource = null;
        this.responces.clear();
        this.responces = null;
    }

    public void destroy() {
        this.resource.destroy();
        this.executorService.shutdown();
        this.resource = null;
        this.password = null;
        System.out.println("Redis desligado com sucesso!");
    }

    public JedisPool getResource() {
        return this.resource;
    }

    @SuppressWarnings("all")
    public void loadResponses(Class<? extends RedisResponceAbstract>... cacheClass) throws Exception {
        for (Class<? extends RedisResponceAbstract> clazz : cacheClass) {
            RedisResponceAbstract cacheClazz;
            try {
                cacheClazz = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            responces.add(cacheClazz);
        }
    }

    public void setupChannel() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(()-> connection.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String chanel, String value) {
                responces.stream().filter(redisResponceAbstract -> redisResponceAbstract.getChannel().equals(chanel)).findFirst().ifPresent(redisResponceAbstract -> {
                    byte[] byteArrayFromString = value.getBytes();
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayFromString);
                    DataInputStream byteArrayDataInput = new DataInputStream(byteArrayInputStream);
                    try {
                        String key = byteArrayDataInput.readUTF();
                        redisResponceAbstract.setupAction(key, byteArrayDataInput);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            public void onPMessage(String s, String s1, String s2) {
            }

            @Override
            public void onSubscribe(String s, int i) {
                // Não é necessário autenticar novamente aqui
            }

            @Override
            public void onUnsubscribe(String s, int i) {
            }

            @Override
            public void onPUnsubscribe(String s, int i) {
            }

            @Override
            public void onPSubscribe(String s, int i) {
            }
        }, "proxiedprofile"));
    }

    public void sendMessage(String channel, ByteArrayOutputStream byteArrayDataOutput) {
        executorService.submit(() -> {
            Jedis connectionNew = this.resource.getResource();
            try {
                connectionNew.auth(this.password);
                connectionNew.getClient().setTimeout(1000);
                String finalByte = byteArrayDataOutput.toString();
                connectionNew.publish(channel, finalByte);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connectionNew.disconnect();
            }
        });
    }
}
