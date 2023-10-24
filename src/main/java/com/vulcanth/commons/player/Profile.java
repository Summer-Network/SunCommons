package com.vulcanth.commons.player;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.game.Game;
import com.vulcanth.commons.lobby.SpawnManager;
import com.vulcanth.commons.nms.NmsManager;
import com.vulcanth.commons.player.cache.CacheAbstract;
import com.vulcanth.commons.player.cache.collections.PlayerPreferencesCache;
import com.vulcanth.commons.player.cash.CashManager;
import com.vulcanth.commons.player.hotbar.HotbarItemObject;
import com.vulcanth.commons.player.preferences.PreferencesEnum;
import com.vulcanth.commons.player.role.Role;
import com.vulcanth.commons.player.role.RoleEnum;
import com.vulcanth.commons.player.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    private Game game;
    private ScoreboardManager scoreboard;
    private String hotbarKey;
    private List<HotbarItemObject> hotbarItens;
    private CashManager cashManager;

    public Profile(String name) {
        this.name = name;
        this.cache = new ArrayList<>();
        this.game = null;
        this.scoreboard = null;
        this.hotbarKey = null;
        this.hotbarItens = null;
    }

    public void refreshPlayer() {
        Player player = getPlayer();
        player.setExp(0.0F);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setMaxHealth(20.0D);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        NmsManager.sendTitle(player, "", "", 0, 0, 0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.closeInventory();
        player.setGameMode(GameMode.ADVENTURE);
        if (this.game == null) { //Executar apenas caso o jogador esteja no lobby!
            RoleEnum role = Role.findRole(player);
            if (role.canFly()) {
                player.setAllowFlight(true);
                player.setFlying(true);
            }

            player.teleport(SpawnManager.getSpawnLocation());
        }

        refreshPlayers();
    }

    @Deprecated
    public void refreshPlayers() {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), ()-> {
            Player player = getPlayer();
            for (Player online : Bukkit.getOnlinePlayers().stream().filter(player1 -> !player1.equals(player)).collect(Collectors.toList())) {
                Profile profile = Profile.loadProfile(online.getName());
                if (profile != null) {
                    if (profile.getGame() == null) { //Significa que o jogador está no lobby
                        RoleEnum role = Role.findRole(online);
                        RoleEnum roleP = Role.findRole(player);
                        PlayerPreferencesCache cache = getCache(PlayerPreferencesCache.class);
                        if (cache.getPreference(PreferencesEnum.SHOW_PLAYERS)) {
                            showPlayer(player, online);
                        } else {
                            if (role.isAlwaysVisible()) {
                                showPlayer(player, online);
                            } else {
                                hidePlayer(player, online);
                            }

                        }

                        if (roleP.isAlwaysVisible()) {
                            showPlayer(online, player);
                        } else {
                            if (!profile.getCache(PlayerPreferencesCache.class).getPreference(PreferencesEnum.SHOW_PLAYERS)) {
                                hidePlayer(online, player);
                            } else {
                                showPlayer(online, player);
                            }
                        }
                    } else { //Jogadores em partida
                        if (online.getWorld().equals(player.getWorld()) && Objects.requireNonNull(Profile.loadProfile(online.getName())).getGame().equals(this.game)) { //Significa que está na mesma partida
                            showPlayer(player, online);
                            showPlayer(online, player);
                        } else {
                            hidePlayer(player, online);
                            hidePlayer(online, player);
                        }
                    }
                }
            }
        });
    }

    private void showPlayer(Player player, Player online) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()-> player.showPlayer(online));
    }

    private void hidePlayer(Player player, Player online) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()-> player.hidePlayer(online));
    }

    @SuppressWarnings("all")
    public void loadCaches(boolean async, Class<? extends CacheAbstract>... cacheClass) throws Exception {
        Profile profile = this;
        Runnable task = () -> {
            for (Class<? extends CacheAbstract> clazz : cacheClass) {
                CacheAbstract cacheClazz;
                try {
                    Constructor<? extends CacheAbstract> constructor = clazz.getConstructor(Profile.class);
                    cacheClazz = constructor.newInstance(this);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }

                cache.add(cacheClazz);
            }

            this.cashManager = new CashManager(this);
        };

        if (async) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), task);
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), task);
        this.cashManager = new CashManager(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends CacheAbstract> T getCache(Class<T> classCache) {
        return (T) this.cache.stream().filter(cacheAbstract -> cacheAbstract.getClass().toString().equals(classCache.toString())).findFirst().orElse(null);
    }

    public void destroy(boolean async) {
        this.cache.forEach(cacheAbstract -> cacheAbstract.save(async));
        if (this.scoreboard != null) {
            this.scoreboard.destroy();
        }
        this.cache = null;
        this.scoreboard = null;
        this.cashManager = null;
        this.hotbarKey = null;
        PROFILES.remove(this.name);
        this.name = null;
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

    public void setGame(Game game) {
        this.game = game;
    }

    public void setScoreboard(ScoreboardManager scoreboard) {
        this.scoreboard = scoreboard;
    }

    @SuppressWarnings("unchecked")
    public <T extends Game> T getGame(Class<T> gameClass) {
        return gameClass.toString().equals(this.game.getClass().toString()) ? (T) game : null;
    }

    public Game getGame() {
        return this.game;
    }

    public ScoreboardManager getScoreboard() {
        return this.scoreboard;
    }

    public void sethotbarKey(String key, List<HotbarItemObject> hotbarItens) {
        this.hotbarKey = key;
        this.hotbarItens = hotbarItens;
    }

    public String getHotbarKey() {
        return this.hotbarKey;
    }

    public CashManager getCashManager() {
        return this.cashManager;
    }

    public void refreshHotbar() {
        if (hotbarItens != null) {
            hotbarItens.forEach(hotbarItemObject -> hotbarItemObject.updateItem(this.getPlayer()));
        }
    }
}
