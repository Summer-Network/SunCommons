package com.summer.commons.player.cash;

import com.summer.commons.player.cache.collections.PlayerInformationsCache;
import com.summer.commons.player.Profile;

public class CashManager {

    private final PlayerInformationsCache cache;

    public CashManager(Profile profile) {
        this.cache = profile.getCache(PlayerInformationsCache.class);
    }

    public Long getCash() {
        try {
            return Long.valueOf(this.cache.getInformation("cash"));
        } catch (Exception e) {
            return 0L;
        }
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
