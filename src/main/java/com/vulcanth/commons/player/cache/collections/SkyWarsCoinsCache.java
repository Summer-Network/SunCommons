package com.vulcanth.commons.player.cache.collections;

import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.CacheAbstract;
import com.vulcanth.commons.storage.redisupdater.collections.ProfileSkinUpdater;
import simple.JSONArray;
import simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SkyWarsCoinsCache extends CacheAbstract {

    public SkyWarsCoinsCache(Profile profile) {
        super("VulcanthSkyWars", "COINS", 0L, null, profile);
    }

    public Long getCoins() {
        return this.getAsLong();
    }

    public void addCoins(Long value) {
        this.setValueCache(getCoins() + value, false);
    }

    public void removeCoins(Long value) {
        this.setValueCache(getCoins() - value, false);
    }

    public void setCoins(Long value) {
        this.setValueCache(value, false);
    }

}
