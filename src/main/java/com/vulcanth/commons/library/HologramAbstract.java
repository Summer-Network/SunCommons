package com.vulcanth.commons.library;

import com.vulcanth.commons.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class HologramAbstract implements Listener {

    @SuppressWarnings("unchecked")
    public static void setupHologram(Class<? extends HologramAbstract>... hologramClasses) {
        for (Class<? extends HologramAbstract> clazz : hologramClasses) {
            try {
                clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public HologramAbstract() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }
}
