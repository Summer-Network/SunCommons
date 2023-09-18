package com.vulcanth.commons.listeners.collections;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.library.holograms.Hologram;
import com.vulcanth.commons.library.holograms.HologramLibrary;
import com.vulcanth.commons.listeners.ListenersAbstract;
import com.vulcanth.commons.lobby.SpawnManager;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.collections.PlayerDeliveryCache;
import com.vulcanth.commons.player.cache.collections.PlayerInformationsCache;
import com.vulcanth.commons.player.cache.collections.PlayerPreferencesCache;
import com.vulcanth.commons.player.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PlayerJoinEvents extends ListenersAbstract {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yy 'às' HH:mm:ss", new Locale("pt", "BR"));

    static {
        SDF.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    }

    /*
    No evento de pré login do jogador, ele carrega o seu perfil
    É de extrema importância que sempre registre as classes que servirão de cache no evento de pré login!
    **/

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String name = event.getName();
        try {
            Profile.createProfile(name).loadCaches(false, PlayerInformationsCache.class, PlayerPreferencesCache.class, PlayerDeliveryCache.class);
        } catch (Exception e) {
            event.setKickMessage("§cOcorreu enquanto carregavamos o seu perfil!");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.loadProfile(player.getName());
        if (profile == null || profile.getCache(PlayerInformationsCache.class) == null) {
            player.kickPlayer("§cOops...\n§cocorreu um erro enquanto carregavamos o seu perfil!");
        }
        if (profile != null) {
            profile.refreshPlayer();
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), profile::refreshPlayer, 3L);

            PlayerInformationsCache cache = profile.getCache(PlayerInformationsCache.class);
            if (cache.getInformation("firstLogin").isEmpty()) { //Caso ele não tenha a data do primeiro login, ele irá setar automaticamente
                cache.updateInformation("firstLogin", SDF.format(new Date()));
            }

            cache.updateInformation("lastLogin", SDF.format(new Date())); //Atualizando a informação do último login no cache, que posteriormente será salvo na DB
        }
        HologramLibrary.createHologram(profile.getPlayer(), SpawnManager.getSpawnLocation().clone().add(0, 2, 0), "todo jv é viado");
        event.setJoinMessage(null);
    }

}
