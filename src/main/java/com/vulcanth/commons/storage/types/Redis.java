package com.vulcanth.commons.storage.types;

import com.vulcanth.commons.Main;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Redis {

    private JedisPool resource;
    private String password;

    public Redis(String password, String host, String port) {
        this.password = password;
        Jedis jedis = null;
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(30);
            config.setMinIdle(5);
            config.setMaxIdle(10);
            config.setMaxWait(3000);
            this.resource = new JedisPool(config, host, Integer.parseInt(port));
            jedis = resource.getResource();
        } catch (Exception e) {
            Main.getInstance().sendMessage("Algo deu errado enquanto conectavamos ao redis...", '4');
            Bukkit.shutdown();
        } finally {
            closeConnection(jedis);
        }
    }

    public Jedis openConnection() {
        Jedis connection = null;
        try {
            connection = this.resource.getResource();
            connection.auth(this.password);
            connection.getClient().setTimeout(1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }

        return connection;
    }

    public void closeConnection() {
        this.resource.destroy();
        this.resource = null;
    }

    private void closeConnection(Jedis jedis) {
        if (jedis != null) {
            jedis.disconnect();
        }
    }

    public void destroy() {
        this.resource.destroy();
        this.resource = null;
        this.password = null;
        Main.getInstance().sendMessage("MySQL desligado com sucesso!", 'e');
    }

    public JedisPool getResource() {
        return this.resource;
    }
}
