package org.nebula.core.bukkit.library.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.nebula.core.bukkit.Main;

@Getter
public abstract class MenuPlayer extends Menu implements Listener {

  protected final Player player;

  public MenuPlayer(Player player, String title, int rows) {
    super(title, rows);
    this.player = player;

    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }

  public void open() {
    player.openInventory(getInventory());
  }

  public abstract void onClick(InventoryClickEvent evt);
  public abstract void onClose();

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(player)) {
      onClose();
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(player) && evt.getInventory().equals(getInventory())) {
      onClose();
    }
  }

  @EventHandler
  public void onIntentoryClick(InventoryClickEvent evt) {
    if (!evt.getInventory().equals(getInventory())) {
      return;
    }

    evt.setCancelled(true);
    if (evt.getClickedInventory() == null || !evt.getClickedInventory().equals(getInventory()) ||
        evt.getCurrentItem() == null || evt.getCurrentItem().getType() == Material.AIR
    ) return;

    onClick(evt);
  }
}
