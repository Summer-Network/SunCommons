package com.summer.commons.storage.redisupdater.collections;

import com.summer.commons.storage.redisupdater.RedisUpdaterAbstract;

public class ProfileInformationsUpdater extends RedisUpdaterAbstract {

    public ProfileInformationsUpdater(String key) {
        super("VulcanthProfiles>" + key);
    }

}
