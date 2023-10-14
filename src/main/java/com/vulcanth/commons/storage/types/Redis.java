package com.vulcanth.commons.storage.types;

import com.vulcanth.commons.storage.redisresponces.RedisResponseAbstract;
import com.vulcanth.commons.storage.redisresponces.collections.PlayerRoleUpdater;
import com.vulcanth.commons.storage.redisresponces.collections.ProxiedUpdater;
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
    private List<RedisResponseAbstract> responses;
    private Jedis connection;

    public Redis(String password, String host, String port, boolean isBungee) {
        this.password = password;
        this.responses = new ArrayList<>();
        setupRedisConnection(host, port);

        try {
            loadResponses(ProxiedUpdater.class, PlayerRoleUpdater.class);
            setupChannel();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar respostas e canal Redis", e);
        }
    }

    private void setupRedisConnection(String host, String port) {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(30);
            config.setMinIdle(5);
            config.setMaxIdle(10);
            config.setMaxWaitMillis(3000);
            this.resource = new JedisPool(config, host, Integer.parseInt(port));
            openConnection();
        } catch (Exception e) {
            handleConnectionError(e);
        }
    }

    private void handleConnectionError(Exception e) {
        e.printStackTrace();
        // Trate o erro de conexão aqui, por exemplo, registre um erro ou aja de acordo com o isBungee
    }

    public void openConnection() {
        try {
            this.connection = this.resource.getResource();
            this.connection.auth(this.password);
            this.connection.getClient().setConnectionTimeout(5000);
        } catch (Exception e) {
            handleConnectionError(e);
        }
    }

    public void closeConnection() {
        if (this.resource != null) {
            this.resource.destroy();
            this.resource = null;
        }
        if (this.responses != null) {
            this.responses.clear();
            this.responses = null;
        }
    }

    public void destroy() {
        closeConnection();
        this.password = null;
        System.out.println("Redis desligado com sucesso!");
    }

    public JedisPool getResource() {
        return this.resource;
    }

    public void loadResponses(Class<? extends RedisResponseAbstract>... cacheClasses) {
        for (Class<? extends RedisResponseAbstract> clazz : cacheClasses) {
            try {
                RedisResponseAbstract cacheClazz = clazz.getDeclaredConstructor().newInstance();
                responses.add(cacheClazz);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao carregar respostas", e);
            }
        }
    }

    public void setupChannel() {
        JedisPubSub pubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                handleChannelMessage(channel, message);
            }
        };

        new Thread(() -> {
            try {
                connection.subscribe(pubSub, "proxiedprofile", "playerrole");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleChannelMessage(String channel, String value) {
        if (channel == null || value == null) {
            return;
        }

        responses.stream().filter(response -> response.getChannel().equals(channel)).findFirst().ifPresent(response -> {
            byte[] byteArrayFromString = value.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayFromString);
            DataInputStream byteArrayDataInput = new DataInputStream(byteArrayInputStream);
            try {
                String key = byteArrayDataInput.readUTF();
                response.setupAction(key, byteArrayDataInput);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar mensagem do canal", e);
            }
        });
    }

    public void sendMessage(String channel, ByteArrayOutputStream byteArrayDataOutput) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executorService.submit(() -> {
            Jedis connectionNew = this.createConnection();
            try {
                String finalByte = byteArrayDataOutput.toString();
                connectionNew.publish(channel, finalByte);
            } catch (Exception e) {
                handleConnectionError(e);
            } finally {
                if (connectionNew != null) {
                    connectionNew.disconnect();
                }
            }
        });
    }

    public Jedis createConnection() {
        Jedis jedis = this.resource.getResource();
        jedis.auth(this.password);
        jedis.getClient().setConnectionTimeout(5000);
        return jedis;
    }
}
