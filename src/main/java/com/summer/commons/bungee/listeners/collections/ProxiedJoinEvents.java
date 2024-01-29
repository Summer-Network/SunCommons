package com.summer.commons.bungee.listeners.collections;

import com.summer.commons.storage.tables.collections.SkinTable;
import com.summer.commons.bungee.BungeeMain;
import com.summer.commons.bungee.listeners.ListenersAbstract;
import com.summer.commons.bungee.process.InfoProcess;
import com.summer.commons.bungee.proxied.ProxiedProfile;
import com.summer.commons.bungee.proxied.cache.collections.PlayerInformationsCache;
import com.summer.commons.bungee.proxied.cache.collections.PlayerPreferencesCache;
import com.summer.commons.bungee.proxied.role.ProxiedRole;
import com.summer.commons.bungee.proxied.role.ProxiedRoleEnum;
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

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.stream.Collectors;

public class ProxiedJoinEvents extends ListenersAbstract {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(PreLoginEvent event) {
        String player = event.getConnection().getName();
        try {
            ProxiedProfile profile = ProxiedProfile.loadProfile(player);
            if (profile == null) {
                ProxiedProfile.createProfile(player).loadCaches(false, PlayerInformationsCache.class, PlayerPreferencesCache.class);
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
                player.disconnect(TextComponent.fromLegacyText("§c§lVULCANTH - MANUTENÇÃO\n\n§cAtualmente estamos em manutenção, aguarde para mais informações\n§cem nosso site: www.vulcanth.com"));
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

            URL url;
            StringBuilder builder = new StringBuilder();

            try {
                url = new URL("https://api.mojang.com/users/profiles/minecraft/" + e.getConnection().getName());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                for (String line : new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.toList())) {
                    builder.append(line.replace(" ", "").replace("{", "").replace("}", ""));
                }

                String uuid = builder.toString().split("\"id\":\"")[1].split("\"")[0];

                url = new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", uuid));
                connection = (HttpsURLConnection) url.openConnection();
                builder = new StringBuilder();

                for (String line : new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.toList())) {
                    builder.append(line.replace(" ", "").replace("{", "").replace("}", ""));
                }

                skin = builder.toString().split("\"properties\":")[1].replace("[", "").split("]")[0].split("\"value\":\"")[1].split("\"")[0];
                signature = builder.toString().split("\"properties\":")[1].replace("[", "").split("]")[0].split("\"signature\":\"")[1].split("\"")[0];
            } catch (IOException ex) {
                skin = "ewogICJ0aW1lc3RhbXAiIDogMTYxMjU1OTc5ODMwOCwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZiOTAzMjBjN2FlOGFmNjE0ZTFmNmRiM2Y5MDdmNDBmNjdlNjY4MGUxZWI4N2U5MDNlYmI5YjI2Zjk0YzljMjQiCiAgICB9CiAgfQp9";
                signature = "LjxNQagEnWdrNSVk9bM0iR/Sa/3xNOZwTxH61Ky5DSa+fRHmXvA8bYRu5usLIRWr9O89ObC3kiOLy8MbQFikERcXqgpZMgHh0GGQW+SEo4JyjI3iE3fT5v2YV1JFeSmGBYRy0v38osV+JfLapps39PKddwkY++19IlDWUQqskyDVyin2JdcNK7naeFxubxEX6R4QLqa5NHOdLU6Qdmw+i8kwJXw7ah/s8RhvBQlP/vm3URrunG9SvcYNfzw+c/7mHmKECe89xNjGpM9PvCFP8iZJd5XJIwLqBltuZaVHDyL7sMMBuZ853qjGt7cvGqqyDBgrdvkbKFCdV2GHIUE4pj6lbDmli/BBQtOP4GCkg/XSZB/J9gu00VgmVFJYSntHqRoHhh1rFM01hm91UpNzHOG13rqUXGdTl8QxxMaj1205h6JKRjNFJqDjJFJ/VmQ9zBMMDRZhRPFMwCnXYriJJbsaWVpKxs26Krz5oRIufqNdbhDH0BCwsA2b1gjPNBPl3hPcH5oVb8hYy9G9dbpnSIP5dbGCHHFX65idP2Dq3HvKA9gO5rWhqY+UVdlduzaOJwFLUPiVL4nTpeLmP+ya+LYYnkhY4FrjFAEzaVBpnWil+gCyz0radMpon1wrV7ytBFQR9fj1piYCMvvhgVTGCPAMPgXf1hME7LgX2G1ofyQ=";
            }

            if (SkinTable.getInformation(e.getConnection().getName(), "SKININFO") != "") {
                String skinName = InfoProcess.processSkinInfo((String) SkinTable.getInformation(e.getConnection().getName(), "SKININFO"));
                if (!skinName.equals("") && skinName.split(";").length > 1) {
                    skin = skinName.split(" ; ")[1];
                    signature = skinName.split(" ; ")[2];
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


