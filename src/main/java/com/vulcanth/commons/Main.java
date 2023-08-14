package com.vulcanth.commons;

import com.vulcanth.commons.commands.CommandsAbstract;
import com.vulcanth.commons.commands.collections.FireworkCommand;
import com.vulcanth.commons.commands.collections.SpawnCommands;
import com.vulcanth.commons.commands.collections.VoarCommands;
import com.vulcanth.commons.model.delivery.Delivery;
import com.vulcanth.commons.listeners.ListenersAbstract;
import com.vulcanth.commons.listeners.collections.PlayerInteractEvents;
import com.vulcanth.commons.listeners.collections.PlayerJoinEvents;
import com.vulcanth.commons.listeners.collections.PlayerQuitEvents;
import com.vulcanth.commons.lobby.SpawnManager;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.plugin.VulcanthPlugins;

public class Main extends VulcanthPlugins {

    @Override
    public void loadPlugin() {
        Delivery.makeDeliveries();

        getVulcanthConfig().setupConfigs("config.yml");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void enablePlugin() {
        CommandsAbstract.setupComands(SpawnCommands.class, VoarCommands.class, FireworkCommand.class);
        ListenersAbstract.setupListeners(PlayerJoinEvents.class, PlayerQuitEvents.class, PlayerInteractEvents.class); //Um simples sistema para registrar as classes de eventos sem precisar repitir o código diversas vezes

        SpawnManager.setupLocation();
        this.sendMessage("O plugin iniciou com sucesso!");
    }

    @Override
    public void disablePlugin() {
        Profile.loadProfiles().forEach(profile -> profile.destroy(false)); //Enquanto não salvar todos os perfils em cache, ele não deixará o servidor desligar
        
        this.sendMessage("O plugin desligou com sucesso!", 'c');
    }

}
