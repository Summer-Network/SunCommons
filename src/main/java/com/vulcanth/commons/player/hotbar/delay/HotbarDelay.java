package com.vulcanth.commons.player.hotbar.delay;

import java.text.DecimalFormat;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

public class HotbarDelay {

    private static final WeakHashMap<String, Long> DELAY_CACHE = new WeakHashMap<>();
    private static final DecimalFormat DF = new DecimalFormat("##.#");

    public static void addDelayForPlayer(String player, Long delay) {
        DELAY_CACHE.put(player, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(delay));
    }

    public static boolean hasDelay(String player) {
        return DELAY_CACHE.containsKey(player) && (((DELAY_CACHE.get(player) - System.currentTimeMillis()) / 1000.0) > 0.1);
    }

    public static String getDelay(String player) {
        return DF.format((DELAY_CACHE.get(player) - System.currentTimeMillis()) / 1000.0);
    }
}
