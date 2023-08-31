package com.vulcanth.commons.player.cache.collections;

import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.CacheAbstract;

public class PlayerDeliveryCache extends CacheAbstract {

    public PlayerDeliveryCache(Profile profile) {
        super("VulcanthProfiles", "REWARDS", "{}", profile);
    }

}
