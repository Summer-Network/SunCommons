package com.vulcanth.commons.player.cash;

import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.collections.PlayerInformationsCache;

public class CashManager {

    private final PlayerInformationsCache cache;

    public CashManager(Profile profile) {
        this.cache = profile.getCache(PlayerInformationsCache.class);
    }

    public Long getCash() {
        return Long.valueOf(this.cache.getInformation("cash"));
    }

    public void addCash(Long value) {
        long finalValue = getCash() + value;
        this.cache.updateInformation("cash", String.valueOf(finalValue));
    }

    public void setCash(Long value) {
        this.cache.updateInformation("cash", String.valueOf(value));
    }

    public void removeCash(Long value) {
        long finalValue = getCash() - value;
        this.cache.updateInformation("cash", String.valueOf(finalValue));
    }

}
