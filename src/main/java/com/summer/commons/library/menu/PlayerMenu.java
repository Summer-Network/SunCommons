package com.summer.commons.library.menu;

import com.summer.commons.library.MenuAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public abstract class PlayerMenu extends MenuAbstract {

    private final Player player;

    public PlayerMenu(Player player, Integer slots) {
        super(slots);
        this.player = player;
    }

    public PlayerMenu(Player player, Integer slots, String title) {
        super(slots, title);
        this.player = player;
    }

    public void open() {
        this.player.openInventory(this.getInventory());
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        this.onPlayerCloseMenu(event);
    }

    @EventHandler
    public void onPlayerInteractInventory(InventoryClickEvent event) {
        this.onPlayerInteractMenu(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.onPlayerQuitOnMenu(event);
    }

    public abstract void onPlayerCloseMenu(InventoryCloseEvent event);
    public abstract void onPlayerInteractMenu(InventoryClickEvent event);
    public abstract void onPlayerQuitOnMenu(PlayerQuitEvent event);
    public abstract void setupItens(Player player);
    public abstract void destroy();
}
