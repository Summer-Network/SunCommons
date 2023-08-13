package org.nebula.core.bukkit.library.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
public class Menu {

  private final Inventory inventory;

  public Menu(String title, int rows) {
    inventory = Bukkit.createInventory(null, 9 * rows, title);
  }

  public void addItems(ItemStack... items) {
    inventory.addItem(items);
  }

  public void setItem(int slot, ItemStack item) {
    inventory.setItem(slot, item);
  }

  public void open(Player player) {
    player.openInventory(inventory);
  }
}
