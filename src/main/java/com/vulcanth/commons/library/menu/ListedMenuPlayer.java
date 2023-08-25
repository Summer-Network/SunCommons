package com.vulcanth.commons.library.menu;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

/**
 * @author Onyzn
 */
@Getter
public abstract class ListedMenuPlayer extends ListedMenu implements Listener {

  protected final Player player;
  protected int currentPage;

  public ListedMenuPlayer(Player player, String title, int rows) {
    super(title, rows);
    this.player = player;
  }

  public Inventory getCurrentInventory() {
    return this.getPages().get(currentPage).getInventory();
  }

  public void openNext() {
    currentPage++;
    this.open(player, currentPage);
  }

  public void openPrevious() {
    currentPage--;
    this.open(player, currentPage);
  }

  public void open(int page) {
    currentPage = page;
    this.open(player, currentPage);
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
    if (evt.getInventory().equals(getCurrentInventory()) && evt.getInventory().equals(getCurrentInventory())) {
      onClose();
    }
  }

  @EventHandler
  public void onIntentoryClick(InventoryClickEvent evt) {
    if (!evt.getInventory().equals(getCurrentInventory())) {
      return;
    }

    evt.setCancelled(true);
    if (evt.getClickedInventory() == null ||
        !evt.getClickedInventory().equals(getCurrentInventory()) ||
        evt.getCurrentItem() == null || evt.getCurrentItem().getType() == Material.AIR
    ) return;

    if (evt.getSlot() == this.getCurrentInventory().getSize() - 4) {
      openNext();
    } else if (evt.getSlot() == this.getCurrentInventory().getSize() - 6) {
      openPrevious();
    } else {
      onClick(evt);
    }
  }
}
