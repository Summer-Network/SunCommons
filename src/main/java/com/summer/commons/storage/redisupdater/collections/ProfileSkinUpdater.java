package com.summer.commons.storage.redisupdater.collections;

import com.summer.commons.storage.redisupdater.RedisUpdaterAbstract;

public class ProfileSkinUpdater extends RedisUpdaterAbstract {
    public ProfileSkinUpdater(String key) {
        super("VulcanthSkins>" + key);
    }

}
