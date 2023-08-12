package com.vulcanth.commons.player;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.player.cache.CacheAbstract;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Profile {
    private static final ConcurrentHashMap<String, Profile> PROFILES = new ConcurrentHashMap<>();

    public static Profile createProfile(String name) {
        Profile profile = new Profile(name);
        PROFILES.put(name, profile);
        return profile;
    }

    public static Profile loadProfile(String name) {
        if (PROFILES.containsKey(name)) {
            return PROFILES.get(name);
        }

        return null;
    }


    //Virtual Profile é o perfil passageiro, que você só pega ele naquele instante e ele sumirá do GC do Java, pois ele não fica em Cache
    public static VirtutalProfile loadVirtualProfile(String name) {
        return null;
    }

    public static Collection<Profile> loadProfiles() {
        return PROFILES.values();
    }

    private String name;
    private List<CacheAbstract> cache;

    public Profile(String name) {
        this.name = name;
        this.cache = new ArrayList<>();
    }

    @SuppressWarnings("all")
    public void loadCaches(boolean async, Class<? extends CacheAbstract>... cacheClass) throws Exception {
        Profile profile = this;
        Runnable task = () -> {
            for (Class<? extends CacheAbstract> clazz : cacheClass) {
                CacheAbstract cacheClazz;
                try {
                    cacheClazz = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                cacheClazz.setProfile(profile);
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
        this.name = null;
        this.cache.forEach(cacheAbstract -> cacheAbstract.save(async));
        this.cache = null;
    }

    public String getName() {
        return this.name;
    }

    public Player getPlayer() {
        try {
            return Bukkit.getPlayer(this.name);
        } catch (Exception e) {
            return null;
        }
    }
}
