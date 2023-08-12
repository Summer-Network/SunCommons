package com.vulcanth.commons.listeners;

import com.vulcanth.commons.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class ListenersAbstract implements Listener {

    @SuppressWarnings("unchecked")
    public static void setupListeners(Class<? extends ListenersAbstract>... listenersClasses) {
        for (Class<? extends ListenersAbstract> clazz : listenersClasses) {
            try {
                clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ListenersAbstract() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }
}
