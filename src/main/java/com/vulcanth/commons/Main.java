package com.vulcanth.commons;

import com.vulcanth.commons.listeners.ListenersAbstract;
import com.vulcanth.commons.listeners.collections.PlayerJoinEvents;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.plugin.VulcanthPlugins;

public class Main extends VulcanthPlugins {

    @Override
    public void loadPlugin() {}

    @SuppressWarnings("unchecked")
    @Override
    public void enablePlugin() {
        ListenersAbstract.setupListeners(PlayerJoinEvents.class); //Um simples sistema para registrar as classes de eventos sem precisar repitir o código diversas vezes
        this.sendMessage("O plugin iniciou com sucesso!");
    }

    @Override
    public void disablePlugin() {
        Profile.loadProfiles().forEach(profile -> profile.destroy(false)); //Enquanto não salvar todos os perfils em cache, ele não deixará o servidor desligar
        this.sendMessage("O plugin desligou com sucesso!", 'c');
    }

}