package com.vulcanth.commons.storage.redisupdater.collections;

import com.vulcanth.commons.storage.redisupdater.RedisUpdaterAbstract;

public class ProfileSkinUpdater extends RedisUpdaterAbstract {
    public ProfileSkinUpdater(String key) {
        super("VulcanthSkins>" + key);
    }

}
