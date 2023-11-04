package com.vulcanth.commons.storage.redisupdater.collections;

import com.vulcanth.commons.storage.redisupdater.RedisUpdaterAbstract;

public class ProfileInformationsUpdater extends RedisUpdaterAbstract {

    public ProfileInformationsUpdater(String key) {
        super("VulcanthProfiles>" + key);
    }

}
