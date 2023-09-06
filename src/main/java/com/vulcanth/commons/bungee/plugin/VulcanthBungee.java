package com.vulcanth.commons.bungee.plugin;

import com.vulcanth.commons.utils.ResetAplication;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class VulcanthBungee extends Plugin {

    @Override
    public void onLoad() {
        this.loadPlugin();
        ResetAplication.scheduleShutdown("10:00:00");
    }

    @Override
    public void onEnable() {
        this.enablePlugin();
    }

    @Override
    public void onDisable() {
        this.disablePlugin();
    }

    public abstract void loadPlugin();
    public abstract void enablePlugin();
    public abstract void disablePlugin();

    public void sendMessage(String message, Character color) {
        this.getLogger().info("§" + color + message);
    }

    public void sendMessage(String message) {
        this.getLogger().info("§a" + message);
    }
}
