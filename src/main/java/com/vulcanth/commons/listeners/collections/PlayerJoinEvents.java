package com.vulcanth.commons.listeners.collections;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.library.HologramManager;
import com.vulcanth.commons.library.hologram.Hologram;
import com.vulcanth.commons.library.npc.NPC;
import com.vulcanth.commons.listeners.ListenersAbstract;
import com.vulcanth.commons.nms.NMS;
import com.vulcanth.commons.nms.NmsManager;
import com.vulcanth.commons.nms.collections.NMS_1_8;
import com.vulcanth.commons.nms.entity.EntityNPC_1_8;
import com.vulcanth.commons.nms.npcs.INPCEntity;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.collections.PlayerDeliveryCache;
import com.vulcanth.commons.player.cache.collections.PlayerInformationsCache;
import com.vulcanth.commons.player.cache.collections.PlayerPreferencesCache;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.loadProfile(player.getName());
        if (profile == null || profile.getCache(PlayerInformationsCache.class) == null) {
            player.kickPlayer("§cOops...\n§cocorreu um erro enquanto carregavamos o seu perfil!");
            return;
        }

        PlayerInformationsCache cache = profile.getCache(PlayerInformationsCache.class);
        if (cache.getInformation("firstLogin").isEmpty()) { //Caso ele não tenha a data do primeiro login, ele irá setar automaticamente
            cache.updateInformation("firstLogin", SDF.format(new Date()));
        }

        cache.updateInformation("lastLogin", SDF.format(new Date())); //Atualizando a informação do último login no cache, que posteriormente será salvo na DB
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Location location = event.getPlayer().getLocation();
        Hologram hologram = HologramManager.createHologram(location.clone().add(0, 1.5, 0));
        hologram.addNewLine("NYEL VIADO");
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()-> {
                    NPC npc = new NPC(location, "adadad", "ewogICJ0aW1lc3RhbXAiIDogMTYxMjU1OTc5ODMwOCwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZiOTAzMjBjN2FlOGFmNjE0ZTFmNmRiM2Y5MDdmNDBmNjdlNjY4MGUxZWI4N2U5MDNlYmI5YjI2Zjk0YzljMjQiCiAgICB9CiAgfQp9", "LjxNQagEnWdrNSVk9bM0iR/Sa/3xNOZwTxH61Ky5DSa+fRHmXvA8bYRu5usLIRWr9O89ObC3kiOLy8MbQFikERcXqgpZMgHh0GGQW+SEo4JyjI3iE3fT5v2YV1JFeSmGBYRy0v38osV+JfLapps39PKddwkY++19IlDWUQqskyDVyin2JdcNK7naeFxubxEX6R4QLqa5NHOdLU6Qdmw+i8kwJXw7ah/s8RhvBQlP/vm3URrunG9SvcYNfzw+c/7mHmKECe89xNjGpM9PvCFP8iZJd5XJIwLqBltuZaVHDyL7sMMBuZ853qjGt7cvGqqyDBgrdvkbKFCdV2GHIUE4pj6lbDmli/BBQtOP4GCkg/XSZB/J9gu00VgmVFJYSntHqRoHhh1rFM01hm91UpNzHOG13rqUXGdTl8QxxMaj1205h6JKRjNFJqDjJFJ/VmQ9zBMMDRZhRPFMwCnXYriJJbsaWVpKxs26Krz5oRIufqNdbhDH0BCwsA2b1gjPNBPl3hPcH5oVb8hYy9G9dbpnSIP5dbGCHHFX65idP2Dq3HvKA9gO5rWhqY+UVdlduzaOJwFLUPiVL4nTpeLmP+ya+LYYnkhY4FrjFAEzaVBpnWil+gCyz0radMpon1wrV7ytBFQR9fj1piYCMvvhgVTGCPAMPgXf1hME7LgX2G1ofyQ=");
                    npc.createEntity();
                    npc.getEntity().spawn();
                    npc.getEntity().setShowNick(false);
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()-> {
                        npc.getEntity().setShowNick(false);
                    }, 20);
                }, 20);
        event.setJoinMessage(null);
    }
}
