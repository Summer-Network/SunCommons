package com.vulcanth.commons.bungee.proxied.cache;

import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import simple.JSONArray;
import simple.JSONObject;
import simple.parser.JSONParser;
import simple.parser.ParseException;

public abstract class CacheAbstract {

    private final ProxiedProfile profile;
    private Object valueCache;
    private String key;

    public CacheAbstract(String key, Object defaultValue, ProxiedProfile profile) {
        this.profile = profile;
        this.key = key;
        this.load(defaultValue);
    }

    //Ele carrega as informações de acordo com o que foi estabelecido no objeto
    private void load(Object defaultValue) {
        this.valueCache = defaultValue;
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
}
