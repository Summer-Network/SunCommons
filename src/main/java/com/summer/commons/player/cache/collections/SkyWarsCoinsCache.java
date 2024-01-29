package com.summer.commons.player.cache.collections;

import com.summer.commons.player.Profile;
import com.summer.commons.player.cache.CacheAbstract;

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
