package com.vulcanth.commons.player.cache;

import com.vulcanth.commons.player.Profile;
import simple.JSONArray;
import simple.JSONObject;
import simple.parser.JSONParser;
import simple.parser.ParseException;

public abstract class CacheAbstract {

    private final String table;
    private final String column;
    private Object valueCache;
    private Profile profile;

    public CacheAbstract(String table, String column, Object defaultValue) {
        this.table = table;
        this.column = column;
        this.profile = null;
        this.load(defaultValue);
    }

    //Sava os dados de forma permanente no database
    public void save(boolean async) {

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

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return this.profile;
    }
}
