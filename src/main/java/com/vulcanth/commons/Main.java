package com.vulcanth.commons;

import com.vulcanth.commons.commands.CommandsAbstract;
import com.vulcanth.commons.commands.collections.CashCommand;
import com.vulcanth.commons.commands.collections.SpawnCommand;
import com.vulcanth.commons.commands.collections.VoarCommand;
import com.vulcanth.commons.listeners.ListenersAbstract;
import com.vulcanth.commons.listeners.collections.PlayerInteractEvents;
import com.vulcanth.commons.listeners.collections.PlayerJoinEvents;
import com.vulcanth.commons.listeners.collections.PlayerQuitEvents;
import com.vulcanth.commons.lobby.SpawnManager;
import com.vulcanth.commons.model.ServerManager;
import com.vulcanth.commons.nms.NmsManager;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.plugin.VulcanthPlugins;
import com.vulcanth.commons.storage.Database;
import org.bukkit.Bukkit;

public class Main extends VulcanthPlugins {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void loadPlugin() {
        instance = this;
        getVulcanthConfig().setupConfigs("config.yml");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void enablePlugin() {
        Database.setupDatabase(false);

        CommandsAbstract.setupComands(SpawnCommand.class, VoarCommand.class, CashCommand.class);
        ListenersAbstract.setupListeners(PlayerJoinEvents.class, PlayerQuitEvents.class, PlayerInteractEvents.class); //Um simples sistema para registrar as classes de eventos sem precisar repitir o código diversas vezes

        SpawnManager.setupLocation();
        NmsManager.setupNMS();
        ServerManager.setupServers();

        this.sendMessage("O plugin iniciou com sucesso!");
    }

    @Override
    public void disablePlugin() {
        Profile.loadProfiles().forEach(profile -> profile.destroy(false)); //Enquanto não salvar todos os perfils em cache, ele não deixará o servidor desligar
        Database.disconnect();
        
        this.sendMessage("O plugin desligou com sucesso!", 'c');
    }

}
