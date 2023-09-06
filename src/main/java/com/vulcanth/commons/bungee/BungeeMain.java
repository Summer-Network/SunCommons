package com.vulcanth.commons.bungee;

import com.vulcanth.commons.bungee.plugin.VulcanthBungee;
import com.vulcanth.commons.storage.Database;

public class BungeeMain extends VulcanthBungee {

    private static BungeeMain instance;

    public static BungeeMain getInstance() {
        return instance;
    }

    @Override
    public void loadPlugin() {
        instance = this;
    }

    @Override
    public void enablePlugin() {
        Database.setupDatabase(true);
        this.sendMessage("O plugin iniciou com sucesso!");
    }

    @Override
    public void disablePlugin() {
        this.sendMessage("O plugin desligou com sucesso!");
    }
}
