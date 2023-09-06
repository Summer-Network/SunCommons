package com.vulcanth.commons.bungee.listeners.collections;

import com.vulcanth.commons.bungee.listeners.ListenersAbstract;
import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import com.vulcanth.commons.bungee.proxied.cache.collections.PlayerInformationsCache;
import com.vulcanth.commons.bungee.proxied.cache.collections.PlayerPreferencesCache;
import com.vulcanth.commons.storage.Database;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ProxiedJoinEvents extends ListenersAbstract {

    @EventHandler
    public void onPlayerPreLogin(PostLoginEvent event) throws IOException {
        ProxiedPlayer player = event.getPlayer();
        try {
            ProxiedProfile.createProfile(player.getName()).loadCaches(false, PlayerInformationsCache.class, PlayerPreferencesCache.class);
        } catch (Exception e) {
            player.disconnect(TextComponent.fromLegacyText("§cOcorreu enquanto carregavamos o seu perfil!"));
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream byteArrayDataOutput = new DataOutputStream(byteArrayOutputStream);
        byteArrayDataOutput.writeUTF(player.getName());
        Database.getRedis().sendMessage("proxiedprofile", byteArrayOutputStream);
    }
}
