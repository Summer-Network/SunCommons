package com.vulcanth.commons.library.menu;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.library.MenuAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class PlayerUpdapleMenu extends MenuAbstract {

    private final Player player;
    private BukkitTask task;
    private Integer delay;

    public PlayerUpdapleMenu(Player player, Integer slots) {
        super(slots);
        this.player = player;
    }

    public PlayerUpdapleMenu(Player player, Integer slots, String title) {
        super(slots, title);
        this.player = player;
    }

    public void open() {
        this.player.openInventory(this.getInventory());
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                update(player);
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0L, delay);
    }

    @EventHandler
    private void onPlayerCloseInventory(InventoryCloseEvent event) {
        this.onPlayerCloseMenu(event);
    }

    @EventHandler
    private void onPlayerInteractInventory(InventoryClickEvent event) {
        this.onPlayerInteractMenu(event);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        this.onPlayerQuitOnMenu(event);
    }

    public BukkitTask getThread() {
        return task;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public abstract void onPlayerCloseMenu(InventoryCloseEvent event);
    public abstract void onPlayerInteractMenu(InventoryClickEvent event);
    public abstract void onPlayerQuitOnMenu(PlayerQuitEvent event);
    public abstract void setupItens(Player player);
    public abstract void destroy();
    public abstract void update(Player player);
}
