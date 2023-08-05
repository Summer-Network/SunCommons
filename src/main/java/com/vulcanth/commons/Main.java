package com.vulcanth.commons;

import com.vulcanth.commons.plugin.VulcanthPlugins;

public class Main extends VulcanthPlugins {

    @Override
    public void loadPlugin() {

    }

    @Override
    public void enablePlugin() {
        this.sendMessage("O plugin iniciou com sucesso!");
    }

    @Override
    public void disablePlugin() {
        this.sendMessage("O plugin desligou com sucesso!", 'c');
    }

}