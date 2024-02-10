package com.summer.commons.player.cache.collections;

import com.summer.commons.player.Profile;
import com.summer.commons.player.cache.CacheAbstract;

public class RankUPMoneyCache extends CacheAbstract {

    public RankUPMoneyCache(Profile profile) {
        super("SunRank", "MONEY", 0L, null, profile);
    }

    public Long getMoney() {
        return this.getAsLong();
    }

    public void addMoney(Long value) {
        this.setValueCache(getMoney() + value, false);
    }

    public void removeMoney(Long value) {
        this.setValueCache(getMoney() - value, false);
    }

    public void setMoney(Long value) {
        this.setValueCache(value, false);
    }

}
