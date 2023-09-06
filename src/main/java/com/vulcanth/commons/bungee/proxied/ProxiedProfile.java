package com.vulcanth.commons.bungee.proxied;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.bungee.proxied.cache.CacheAbstract;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;

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
            Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), task);
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), task);
    }

    @SuppressWarnings("unchecked")
    public <T extends CacheAbstract> T getCache(Class<T> classCache) {
        return (T) this.cache.stream().filter(cacheAbstract -> cacheAbstract.getClass().toString().equals(classCache.toString())).findFirst().orElse(null);
    }

    public void destroy(boolean async) {
        PROFILES.remove(this.name);
        this.cache.forEach(cacheAbstract -> cacheAbstract.save(async));
        this.name = null;
        this.cache = null;
    }

    public String getName() {
        return this.name;
    }

    public ProxiedPlayer getPlayer() {
        return BungeeMain.getInstance().getProxy().getPlayer(this.name);
    }
}
