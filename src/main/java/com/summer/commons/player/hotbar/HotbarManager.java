package com.summer.commons.player.hotbar;

import com.summer.commons.Main;
import com.summer.commons.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotbarManager implements Listener {

    private final Map<String, List<HotbarItemObject>> HOTBAR_FOR_KEY;

    public HotbarManager() {
        this.HOTBAR_FOR_KEY = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public List<HotbarItemObject> listItensForKey(String key) {
        return HOTBAR_FOR_KEY.get(key);
    }

    public void setHotbar(Player player, String key) {
        for (HotbarItemObject item : HOTBAR_FOR_KEY.get(key)) {
            item.addItem(player);
        }
    }

    public void registerNewItem(HotbarItemObject item, String keyHotbar) {
        if (HOTBAR_FOR_KEY.containsKey(keyHotbar)) {
            HOTBAR_FOR_KEY.get(keyHotbar).add(item);
            return;
        }

        HOTBAR_FOR_KEY.put(keyHotbar, new ArrayList<>());
        HOTBAR_FOR_KEY.get(keyHotbar).add(item);
    }

    @EventHandler
    public void onPlayerInteractHotbar(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.loadProfile(player.getName());
        if (profile != null) {
            if (profile.getHotbarKey() != null) {
                int slot = player.getInventory().getHeldItemSlot();
                HotbarItemObject item = findItemForSlot(profile.getHotbarKey(), slot);
                if (item != null) {
                    item.setupAction(player);
                    event.setCancelled(true);
                }
            }
        }
    }

    public HotbarItemObject findItemForSlot(String key, int slot) {
        return HOTBAR_FOR_KEY.get(key).stream().filter(hotbarItemObject -> hotbarItemObject.getSlot() == slot).findFirst().orElse(null);
    }
}
