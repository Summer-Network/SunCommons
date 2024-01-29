package com.summer.commons.storage.redisupdater;

import com.summer.commons.storage.Database;
import com.summer.commons.storage.types.Redis;
import redis.clients.jedis.Jedis;
import simple.JSONObject;
import simple.parser.JSONParser;

public class RedisUpdaterAbstract {

    private final String key;
    private final Redis redis;

    public RedisUpdaterAbstract(String key) {
        this.key = key;
        redis = Database.getRedis();
    }

    public Redis getRedis() {
        return this.redis;
    }

    public String getKey() {
        return this.key;
    }

    public void saveContent(String keyInformation, Object value) {
        Jedis connection = this.redis.createConnection();
        try {
            if (!connection.exists(keyInformation)) {
                connection.set(keyInformation, new JSONObject().toJSONString());
            }

            JSONObject object = (JSONObject) new JSONParser().parse(connection.get(keyInformation));
            if (!object.containsKey(this.key)) {
                object.put(this.key, value);
            } else {
                object.replace(this.key, value);
            }

            connection.set(keyInformation, object.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public Object getValue(String keyInformation) {
        Jedis connection = this.redis.createConnection();
        try {
            if (!connection.exists(keyInformation)) {
                return null;
            }

            JSONObject object = (JSONObject) new JSONParser().parse(connection.get(keyInformation));
            return object.get(this.key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        return null;
    }

}
