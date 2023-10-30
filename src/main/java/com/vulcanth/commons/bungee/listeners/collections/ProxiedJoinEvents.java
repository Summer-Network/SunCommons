package com.vulcanth.commons.bungee.listeners.collections;

import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.bungee.listeners.ListenersAbstract;
import com.vulcanth.commons.bungee.process.InfoProcess;
import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import com.vulcanth.commons.bungee.proxied.cache.collections.PlayerInformationsCache;
import com.vulcanth.commons.bungee.proxied.cache.collections.PlayerPreferencesCache;
import com.vulcanth.commons.bungee.proxied.role.ProxiedRole;
import com.vulcanth.commons.bungee.proxied.role.ProxiedRoleEnum;
import com.vulcanth.commons.library.MojangAPI;
import com.vulcanth.commons.storage.tables.collections.SkinTable;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.protocol.Property;

import java.lang.reflect.Field;
import java.util.Objects;

public class ProxiedJoinEvents extends ListenersAbstract {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(PreLoginEvent event) {
        String player = event.getConnection().getName();
        try {
            ProxiedProfile profile = ProxiedProfile.loadProfile(player);
            if (profile == null) {
                ProxiedProfile.createProfile(player).loadCaches(true, PlayerInformationsCache.class, PlayerPreferencesCache.class);
            } else {
                ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("Este jogador existe."));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ProxiedProfile profile = ProxiedProfile.loadProfile(player.getName());

        if (profile == null) {
            player.disconnect(TextComponent.fromLegacyText("§cOcorreu enquanto carregavamos o seu perfil!"));
            return;
        }

        if (BungeeMain.isIsMaintence()) {
            ProxiedRoleEnum role = ProxiedRole.findRoleByID(profile.getCache(PlayerInformationsCache.class).getInformation("role"));
            if (role.getId() >= 4) {
                //player.disconnect(TextComponent.fromLegacyText("§c§lVULCANTH - MANUTENÇÃO\n\n§cAtualmente estamos em manutenção, aguarde para mais informações\n§cem nosso site: www.vulcanth.com"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(LoginEvent e) {
        String skin;
        String signature;
        try {
            PendingConnection pendingConnection = e.getConnection();

            Class<?> initialHandlerClass = pendingConnection.getClass();
            Field loginProfile = initialHandlerClass.getDeclaredField("loginProfile");

            skin = "ewogICJ0aW1lc3RhbXAiIDogMTYxMjU1OTc5ODMwOCwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZiOTAzMjBjN2FlOGFmNjE0ZTFmNmRiM2Y5MDdmNDBmNjdlNjY4MGUxZWI4N2U5MDNlYmI5YjI2Zjk0YzljMjQiCiAgICB9CiAgfQp9";
            signature = "LjxNQagEnWdrNSVk9bM0iR/Sa/3xNOZwTxH61Ky5DSa+fRHmXvA8bYRu5usLIRWr9O89ObC3kiOLy8MbQFikERcXqgpZMgHh0GGQW+SEo4JyjI3iE3fT5v2YV1JFeSmGBYRy0v38osV+JfLapps39PKddwkY++19IlDWUQqskyDVyin2JdcNK7naeFxubxEX6R4QLqa5NHOdLU6Qdmw+i8kwJXw7ah/s8RhvBQlP/vm3URrunG9SvcYNfzw+c/7mHmKECe89xNjGpM9PvCFP8iZJd5XJIwLqBltuZaVHDyL7sMMBuZ853qjGt7cvGqqyDBgrdvkbKFCdV2GHIUE4pj6lbDmli/BBQtOP4GCkg/XSZB/J9gu00VgmVFJYSntHqRoHhh1rFM01hm91UpNzHOG13rqUXGdTl8QxxMaj1205h6JKRjNFJqDjJFJ/VmQ9zBMMDRZhRPFMwCnXYriJJbsaWVpKxs26Krz5oRIufqNdbhDH0BCwsA2b1gjPNBPl3hPcH5oVb8hYy9G9dbpnSIP5dbGCHHFX65idP2Dq3HvKA9gO5rWhqY+UVdlduzaOJwFLUPiVL4nTpeLmP+ya+LYYnkhY4FrjFAEzaVBpnWil+gCyz0radMpon1wrV7ytBFQR9fj1piYCMvvhgVTGCPAMPgXf1hME7LgX2G1ofyQ=";

            String skinName = InfoProcess.processSkinInfo((String) SkinTable.getInformation(e.getConnection().getName(), "SKININFO"));
            if (!Objects.equals(skinName, "")) {
                try {
                    String skinConfig = MojangAPI.getSkinProperty(MojangAPI.getUUID(skinName));
                    Property property = new Property("textures", skinConfig.split(" : ")[1], skinConfig.split(" : ")[2]);
                    LoginResult loginResult = new LoginResult(pendingConnection.getUniqueId().toString(), pendingConnection.getName(), new Property[]{property});

                    loginProfile.setAccessible(true);
                    loginProfile.set(pendingConnection, loginResult);
                    System.out.println("a");
                    return;
                } catch (Exception ex) {
                    Property property = new Property("textures", skin, signature);
                    LoginResult loginResult = new LoginResult(pendingConnection.getUniqueId().toString(), pendingConnection.getName(), new Property[]{property});

                    loginProfile.setAccessible(true);
                    loginProfile.set(pendingConnection, loginResult);
                    ex.printStackTrace();
                }
            }

            Property property = new Property("textures", skin, signature);
            LoginResult loginResult = new LoginResult(pendingConnection.getUniqueId().toString(), pendingConnection.getName(), new Property[]{property});

            loginProfile.setAccessible(true);
            loginProfile.set(pendingConnection, loginResult);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

}


