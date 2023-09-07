package com.vulcanth.commons.bungee.plugin;

import com.vulcanth.commons.utils.ResetAplication;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

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

    @SuppressWarnings("All")
    public static Configuration getYaml(String fileName, String filePath) {
        File file = new File(filePath + "/" + fileName);
        if (!file.exists()) {
            File folder = file.getParentFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            try {
                Files.copy(Objects.requireNonNull(VulcanthBungee.class.getResourceAsStream("/" + fileName)), file.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            return YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
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
