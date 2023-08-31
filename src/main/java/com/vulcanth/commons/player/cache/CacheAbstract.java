package com.vulcanth.commons.player.cache;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.storage.Database;
import com.vulcanth.commons.storage.tables.collections.ProfileTable;
import org.bukkit.Bukkit;
import simple.JSONArray;
import simple.JSONObject;
import simple.parser.JSONParser;
import simple.parser.ParseException;

import java.sql.Connection;

public abstract class CacheAbstract {

    private final String table;
    private final String column;
    private final Profile profile;
    private Object valueCache;

    public CacheAbstract(String table, String column, Object defaultValue, Profile profile) {
        this.table = table;
        this.column = column;
        this.profile = profile;
        this.load(defaultValue);
    }

    //Sava os dados de forma permanente no database
    @Deprecated
    public void save(boolean async) {
        Runnable task = ()-> {
            switch (this.table) {
                case "VulcanthProfiles": {
                    ProfileTable.update(profile.getName(), this.column, this.valueCache);
                    break;
                }
            }
        };

        if (async) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), task);
        } else {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), task);
        }
    }


    //Ele carrega as informações de acordo com o que foi estabelecido no objeto
    private void load(Object defaultValue) {
        if (!Database.getMySQL().conteins(this.table, "NAME", profile.getName())) {
            this.valueCache = defaultValue;
            switch (this.table) {
                case "VulcanthProfiles": {
                    ProfileTable.setDefault(profile.getName());
                    break;
                }
            }
        } else {
            switch (this.table) {
                case "VulcanthProfiles": {
                    this.valueCache = ProfileTable.getInformation(profile.getName(), this.column);
                    break;
                }
            }
        }
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

    public Profile getProfile() {
        return this.profile;
    }
}
