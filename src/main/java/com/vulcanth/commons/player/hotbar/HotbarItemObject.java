package com.vulcanth.commons.player.hotbar;

import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.collections.PlayerPreferencesCache;
import com.vulcanth.commons.player.preferences.PreferencesEnum;
import com.vulcanth.commons.utils.ItemUtils;
import com.vulcanth.commons.utils.StringUtils;
import com.vulcanth.commons.view.ProfileView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class HotbarItemObject {

    private final String item;
    private final int slot;
    private final String action;
    private final HotbarExtension expansion;

    public HotbarItemObject(String item, int slot, String action, HotbarExtension extension) {
        this.item = item;
        this.slot = slot;
        this.action = action;
        this.expansion = extension;
    }

    public int getSlot() {
        return this.slot - 1;
    }

    public ItemStack getItem() {
        return ItemUtils.getItemStackFromString(this.item);
    }

    public String getAction() {
        return this.action;
    }

    public void addItem(Player player) {
        ItemStack finalItem = ItemUtils.getItemStackFromString(expansion.hotbarReplaces(replace(this.item, Objects.requireNonNull(Profile.loadProfile(player.getName())))));
        player.getInventory().setItem(getSlot(), finalItem);
    }

    public void setupAction(Player player) {
        String[] actions = this.action.split(" : ");
        for (String action : actions) {
            String actionType = action.split(">")[0];
            String value = action.split(">")[1];
            if (!setupDefaultActions(player, actionType, value)) {
                expansion.setupActionPersonality(player, actionType, value);
            }
        }
    }

    private boolean setupDefaultActions(Player player, String actionType, String value) {
        Profile profile = Profile.loadProfile(player.getName());
        if (profile != null) {
            switch (actionType) {
                case "tell": {
                    player.sendMessage(StringUtils.formatColors(value));
                    return true;
                }

                case "command": {
                    player.performCommand(value);
                    return true;
                }

                case "action": {
                    switch (value) {
                        case "hideplayers": {
                            profile.getCache(PlayerPreferencesCache.class).changePreference(PreferencesEnum.SHOW_PLAYERS);
                            return true;
                        }
                    }
                }

                case "open": {
                    switch (value) {
                        case "profile": {
                            new ProfileView(player);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private String replace(String itemBase, Profile profile) {
        return itemBase
                .replace("{color_showplayers}", profile.getCache(PlayerPreferencesCache.class).getPreference(PreferencesEnum.SHOW_PLAYERS) ? "10" : "8")
                .replace("{name_showplayers}", profile.getCache(PlayerPreferencesCache.class).getPreference(PreferencesEnum.SHOW_PLAYERS) ? "&aON" : "&cOFF");
    }
}
