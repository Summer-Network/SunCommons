package com.vulcanth.commons.bungee.proxied.cache;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import org.bukkit.Bukkit;
import simple.JSONArray;
import simple.JSONObject;
import simple.parser.JSONParser;
import simple.parser.ParseException;

public abstract class CacheAbstract {

    private final ProxiedProfile profile;
    private Object valueCache;

    public CacheAbstract(Object defaultValue, ProxiedProfile profile) {
        this.profile = profile;
        this.load(defaultValue);
    }

    //Sava os dados de forma permanente no database
    @Deprecated
    public void save(boolean async) {
        Runnable task = ()-> {
        };

        if (async) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), task);
        } else {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), task);
        }
    }


    //Ele carrega as informações de acordo com o que foi estabelecido no objeto
    private void load(Object defaultValue) {
        this.valueCache = defaultValue;
    }

    public void setValueCache(Object value) {
        this.valueCache = value;
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
}
