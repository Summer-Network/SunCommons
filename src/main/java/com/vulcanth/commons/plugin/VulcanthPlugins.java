package com.vulcanth.commons.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class VulcanthPlugins extends JavaPlugin {

    private static VulcanthPlugins instance;

    public static VulcanthPlugins getInstance() {
        return instance;
    }

    private final VulcanthConfig config = new VulcanthConfig(this);

    @Override
    public void onLoad() {
        this.loadPlugin();
        instance = this;
    }

    @Override
    public void onEnable() {
        this.enablePlugin();
    }

    @Override
    public void onDisable() {
        this.disablePlugin();
    }

    public VulcanthConfig getVulcanthConfig() {
        return this.config;
    }

    public void sendMessage(String message, Character color) {
        Bukkit.getConsoleSender().sendMessage("§" + color + "[" + this.getDescription().getName() + "] " + message);
    }

    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage("§a[" + this.getDescription().getName() + "] " + message);
    }

    public abstract void loadPlugin();
    public abstract void enablePlugin();
    public abstract void disablePlugin();

}
