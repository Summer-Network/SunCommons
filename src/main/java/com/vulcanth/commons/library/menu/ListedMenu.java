package com.vulcanth.commons.library.menu;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.library.Menu;
import com.vulcanth.commons.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Onyzn
 */
@Getter
public class ListedMenu implements Listener {

  private final List<Menu> pages;
  private int[] availableSlots = {
      10, 11, 12, 13, 14, 15, 16,
      19, 20, 21, 22, 23, 24, 25,
      28, 29, 30, 31, 32, 33, 34
  };

  public ListedMenu(String title, int rows) {
    this.pages = new ArrayList<>();
    this.pages.add(new Menu(title, rows));

    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }

  public void setAvailableSlots(int... availableSlots) {
    this.availableSlots = availableSlots;
  }

  public void setItems(List<ItemStack> itemsList, Map<Integer, ItemStack> items) {
    int size = pages.get(0).getInventory().getSize();
    int rows = size / 9;
    String title = pages.get(0).getInventory().getTitle();

    int index = 0, page = 0;
    for (ItemStack item : itemsList) {
      if (availableSlots.length == index) {
        this.pages.get(page).setItem(size - 6, ItemBuilder.of(Material.ARROW).display("&aIr para página " + (page + 2)).build());

        index = 0;
        page++;

        if (this.pages.get(page) == null) {
          Menu menu = new Menu(title.replace("{page}", Integer.toString(page)), rows);
          this.pages.get(page).setItem(size - 4, ItemBuilder.of(Material.ARROW).display("&aIr para página " + page).build());
          pages.add(menu);
        }
      }

      Menu menu = pages.get(page);

      if (index == 0) {
        items.forEach(menu::setItem);
      }

      menu.setItem(this.availableSlots[index], item);
      index++;
    }
  }


  public void open(Player player, int page) {
    pages.get(page).open(player);
  }
}
