package com.vulcanth.commons.bungee.listeners;

import com.vulcanth.commons.bungee.BungeeMain;
import net.md_5.bungee.api.plugin.Listener;

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
        BungeeMain.getInstance().getProxy().getPluginManager().registerListener(BungeeMain.getInstance(), this);
    }
}
