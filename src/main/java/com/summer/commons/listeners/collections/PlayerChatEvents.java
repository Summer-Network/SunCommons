package com.summer.commons.listeners.collections;

import com.summer.commons.library.MojangAPI;
import com.summer.commons.listeners.ListenersAbstract;
import com.summer.commons.model.Skin;
import com.summer.commons.model.SkinCacheCommand;
import com.summer.commons.player.Profile;
import com.summer.commons.player.cache.collections.PlayerSkinCache;
import com.summer.commons.player.role.Role;
import com.summer.commons.player.role.RoleEnum;
import com.summer.commons.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlayerChatEvents extends ListenersAbstract {

    private static final Map<String, Long> CACHE = new HashMap<>();

    @EventHandler
    public void onPlayerAsyncChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        Profile profile = Profile.loadProfile(player.getName());
        if (!SkinCacheCommand.isSkinProgress(player.getName()) || profile == null) {
            return;
        }

        event.setCancelled(true);
        String name = event.getMessage().split(" ")[0];

        if (name.equals("SKIN.CANCEL")) {
            player.sendMessage("§cMudança de skin cancelada.");
            SkinCacheCommand.removeSkinProgress(player.getName());
            return;
        }

        if (name.length() <= 1) {
            player.sendMessage("§cNome de jogadores podem conter apenas de 2 a 16 caracteres.");
            SkinCacheCommand.removeSkinProgress(player.getName());
            return;
        }

        player.sendMessage("§aAlterando sua skin para a de " + name + "...");
        boolean success = false;
        try {
            RoleEnum role = Role.findRole(player);
            if (profile.getCache(PlayerSkinCache.class).listSkinsUsed().size() == role.getMaxSkinUse()) {
                player.sendMessage("§cVocê já está no limite máximo de skins.");
                return;
            }

            String skinInfo = MojangAPI.getSkinProperty(MojangAPI.getUUID(name));
            profile.getCache(PlayerSkinCache.class).updateSkinSelected(name, skinInfo.split(" : ")[1] + " ; " + skinInfo.split(" : ")[2]);
            profile.getCache(PlayerSkinCache.class).putSkinUse(name);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage("§cFalha ao encontrar skin do jogador " + name + "!");
        } finally {
            if (success) {
                player.sendMessage("§aSua skin foi alterada com sucesso, relogue para visualisar.");
            }

            SkinCacheCommand.removeSkinProgress(player.getName());
            profile.setSkins(Skin.listSkins(profile));
        }
    }

    private void addCache(String name) {
        CACHE.put(name, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));
    }

    private String getTimeCache(String name) {
        return StringUtils.transformTimeFormated(CACHE.get(name));
    }

    private boolean isCooldown(String name) {
        return CACHE.containsKey(name) || ((double) (CACHE.get(name) - System.currentTimeMillis()) / 1000) <= 0.1;
    }
}
