package com.vulcanth.commons;

import com.vulcanth.commons.commands.CommandsAbstract;
import com.vulcanth.commons.commands.collections.*;
import com.vulcanth.commons.hook.VulcanthHook;
import com.vulcanth.commons.library.npc.listeners.NPCListeners;
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
import com.vulcanth.commons.manager.Manager;
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

        CommandsAbstract.setupComands(SpawnCommand.class, VoarCommand.class, CashCommand.class, HologramCommand.class);
        ListenersAbstract.setupListeners(PlayerJoinEvents.class, PlayerQuitEvents.class, PlayerInteractEvents.class, NPCListeners.class);
        VulcanthHook.setupHooks();
        SpawnManager.setupLocation();
        NmsManager.setupNMS();
        ServerManager.setupServers();
        Manager.isBungeePlugin = false;

        this.sendMessage("O plugin iniciou com sucesso!");
    }

    @Override
    public void disablePlugin() {
        Profile.loadProfiles().forEach(profile -> profile.destroy(false));
        Database.disconnect();
        
        this.sendMessage("O plugin desligou com sucesso!", 'c');
    }

}
