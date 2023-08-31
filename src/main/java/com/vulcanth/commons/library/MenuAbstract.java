package com.vulcanth.commons.library;

import com.vulcanth.commons.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class MenuAbstract implements Listener {

    private final Inventory inventory;

    public MenuAbstract(Integer slots) {
        inventory = Bukkit.createInventory(null, slots * 9);
    }

    public MenuAbstract(Integer slots, String title) {
        inventory = Bukkit.createInventory(null, slots * 9, title);
    }

    public void registerMenu() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void setItem(ItemStack item, Integer slot) {
        this.inventory.setItem(slot, item);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
