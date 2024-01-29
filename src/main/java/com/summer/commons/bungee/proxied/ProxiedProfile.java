package com.summer.commons.bungee.proxied;

import com.summer.commons.bungee.proxied.cache.CacheAbstract;
import com.summer.commons.bungee.BungeeMain;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ProxiedProfile {

    private static final ConcurrentHashMap<String, ProxiedProfile> PROFILES = new ConcurrentHashMap<>();

    public static ProxiedProfile createProfile(String name) {
        ProxiedProfile profile = new ProxiedProfile(name);
        PROFILES.put(name, profile);
        return profile;
    }

    public static ProxiedProfile loadProfile(String name) {
        if (PROFILES.containsKey(name)) {
            return PROFILES.get(name);
        }

        return null;
    }

    public static Collection<ProxiedProfile> loadProfiles() {
        return PROFILES.values();
    }

    private String name;
    private List<CacheAbstract> cache;

    public ProxiedProfile(String name) {
        this.name = name;
        this.cache = new ArrayList<>();
    }

    @SuppressWarnings("all")
    public void loadCaches(boolean async, Class<? extends CacheAbstract>... cacheClass) throws Exception {
        ProxiedProfile profile = this;
        Runnable task = () -> {
            for (Class<? extends CacheAbstract> clazz : cacheClass) {
                CacheAbstract cacheClazz;
                try {
                    Constructor<? extends CacheAbstract> constructor = clazz.getConstructor(ProxiedProfile.class);
                    cacheClazz = constructor.newInstance(this);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }

                cache.add(cacheClazz);
            }
        };

        if (async) {
            BungeeMain.getInstance().getProxy().getScheduler().runAsync(BungeeMain.getInstance(), task);
        } else {
            task.run();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends CacheAbstract> T getCache(Class<T> classCache) {
        return (T) this.cache.stream().filter(cacheAbstract -> cacheAbstract.getClass().toString().equals(classCache.toString())).findFirst().orElse(null);
    }

    public CacheAbstract getCache(String column) {
        return this.cache.stream().filter(cacheAbstract -> cacheAbstract.getKey().equals(column)).findFirst().orElse(null);
    }
    public void destroy() {
        this.cache.forEach(CacheAbstract::saveRedis);
        PROFILES.remove(this.name);
        this.cache = null;
    }

    public String getName() {
        return this.name;
    }

    public ProxiedPlayer getPlayer() {
        return BungeeMain.getInstance().getProxy().getPlayer(this.name);
    }
}
