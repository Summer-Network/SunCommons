package com.vulcanth.commons.bungee.listeners.collections;

import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.bungee.listeners.ListenersAbstract;
import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import com.vulcanth.commons.bungee.proxied.cache.collections.PlayerInformationsCache;
import com.vulcanth.commons.bungee.proxied.cache.collections.PlayerPreferencesCache;
import com.vulcanth.commons.bungee.proxied.role.ProxiedRole;
import com.vulcanth.commons.bungee.proxied.role.ProxiedRoleEnum;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;

public class ProxiedJoinEvents extends ListenersAbstract {

    @EventHandler
    public void onPlayerPreLogin(LoginEvent event) {
        String player = event.getConnection().getName();
        try {
            ProxiedProfile.createProfile(player).loadCaches(true, PlayerInformationsCache.class, PlayerPreferencesCache.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (BungeeMain.isIsMaintence()) {
            ProxiedProfile profile = ProxiedProfile.loadProfile(player.getName());
            ProxiedRoleEnum role = ProxiedRole.findRoleByID(profile.getCache(PlayerInformationsCache.class).getInformation("role"));
            if (role.getId() <= 4) {
                return;
            } else {
                player.disconnect(TextComponent.fromLegacyText("§c§lVULCANTH - MANUTENÇÃO\n\n§cAtualmente estamos em manutenção, aguarde para mais informações\n§cem nosso site: www.vulcanth.com"));
            }
        }

        if (ProxiedProfile.loadProfile(player.getName()) == null) {
            player.disconnect(TextComponent.fromLegacyText("§cOcorreu enquanto carregavamos o seu perfil!"));
        }
    }
}
