package com.vulcanth.commons.bungee;

import com.vulcanth.commons.bungee.commands.CommandsAbstract;
import com.vulcanth.commons.bungee.commands.collections.ManutencaoCommand;
import com.vulcanth.commons.bungee.listeners.ListenersAbstract;
import com.vulcanth.commons.bungee.listeners.collections.ProxiedJoinEvents;
import com.vulcanth.commons.bungee.listeners.collections.ServerEvents;
import com.vulcanth.commons.bungee.plugin.VulcanthBungee;
import com.vulcanth.commons.storage.Database;
import net.md_5.bungee.config.Configuration;

import java.util.Objects;

public class BungeeMain extends VulcanthBungee {

    private static BungeeMain instance;
    private static boolean isMaintence;
    private static Configuration config;

    public static BungeeMain getInstance() {
        return instance;
    }

    public static void setIsMaintence(boolean isMaintence) {
        BungeeMain.isMaintence = isMaintence;
    }

    public static boolean isIsMaintence() {
        return isMaintence;
    }
    public Configuration getConfig() {
        return config;
    }

    @Override
    public void loadPlugin() {
        instance = this;
        isMaintence = Objects.requireNonNull(getYaml("options.yml", "plugins/" + getDescription().getName())).getBoolean("manutencao");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void enablePlugin() {
        Database.setupDatabase(true);

        CommandsAbstract.setupComands(ManutencaoCommand.class);
        ListenersAbstract.setupListeners(ProxiedJoinEvents.class, ServerEvents.class);

        this.sendMessage("O plugin iniciou com sucesso!");
    }

    @Override
    public void disablePlugin() {
        Database.disconnect();

        this.sendMessage("O plugin desligou com sucesso!");
    }
}
