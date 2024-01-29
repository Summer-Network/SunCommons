package com.summer.commons;

import com.summer.commons.commands.collections.*;
import com.summer.commons.hook.VulcanthHook;
import com.summer.commons.lobby.SpawnManager;
import com.summer.commons.nms.NmsManager;
import com.summer.commons.commands.CommandsAbstract;
import com.summer.commons.library.npc.listeners.NPCListeners;
import com.summer.commons.listeners.ListenersAbstract;
import com.summer.commons.listeners.collections.PlayerChatEvents;
import com.summer.commons.listeners.collections.PlayerInteractEvents;
import com.summer.commons.listeners.collections.PlayerJoinEvents;
import com.summer.commons.listeners.collections.PlayerQuitEvents;
import com.summer.commons.model.ServerManager;
import com.summer.commons.player.Profile;
import com.summer.commons.plugin.VulcanthPlugins;
import com.summer.commons.storage.Database;
import com.summer.commons.manager.Manager;
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

        CommandsAbstract.setupComands(SpawnCommand.class, VoarCommand.class, CashCommand.class, HologramCommand.class, SkinCommand.class);
        ListenersAbstract.setupListeners(PlayerJoinEvents.class, PlayerQuitEvents.class, PlayerInteractEvents.class, NPCListeners.class, PlayerChatEvents.class);
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
