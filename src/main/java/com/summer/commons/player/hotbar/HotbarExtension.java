package com.summer.commons.player.hotbar;

import org.bukkit.entity.Player;

public interface HotbarExtension {

    void setupActionPersonality(Player player, String action, String value);
    String hotbarReplaces(String itemReplace);

}
