package com.summer.commons.bungee.proxied.cache;

import com.summer.commons.storage.redisupdater.RedisUpdaterAbstract;
import com.summer.commons.bungee.proxied.ProxiedProfile;
import simple.JSONArray;
import simple.JSONObject;
import simple.parser.JSONParser;
import simple.parser.ParseException;

public abstract class CacheAbstract {

    private final ProxiedProfile profile;
    private final String key;
    private final RedisUpdaterAbstract updater;
    private Object valueCache;


    public CacheAbstract(String key, Object defaultValue, RedisUpdaterAbstract updater, ProxiedProfile profile) {
        this.profile = profile;
        this.key = key;
        this.updater = updater;
        this.load(defaultValue);
    }

    //Ele carrega as informações de acordo com o que foi estabelecido no objeto
    private void load(Object defaultValue) {
        Object value = updater.getValue(profile.getName());
        if (value == null) {
            this.valueCache = defaultValue;
        } else {
            this.valueCache = value;
        }
    }

    public void setValueCache(Object value) {
        this.valueCache = value;
        System.out.println(value);
    }

    public Object getValueCache() {
        return this.valueCache;
    }

    public String getAsString() {
        return (String) this.valueCache;
    }

    public Integer getAsInterger() {
        return (Integer) this.valueCache;
    }

    public Long getAsLong() {
        return (Long) this.valueCache;
    }

    public JSONObject getAsJSONObject() {
        try {
            return (JSONObject) new JSONParser().parse(this.getAsString());
        } catch (ParseException e) {
            return new JSONObject();
        }
    }

    public JSONArray getAsJSONArray() {
        try {
            return (JSONArray) new JSONParser().parse(this.getAsString());
        } catch (ParseException e) {
            return new JSONArray();
        }
    }

    public ProxiedProfile getProfile() {
        return this.profile;
    }

    public String getKey() {
        return this.key;
    }

    public RedisUpdaterAbstract getUpdater() {
        return this.updater;
    }

    public void saveRedis() {
        this.updater.saveContent(this.profile.getName(), this.valueCache);
    }
}
