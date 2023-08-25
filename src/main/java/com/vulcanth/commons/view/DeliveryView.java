package com.vulcanth.commons.view;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.library.menu.MenuPlayer;
import com.vulcanth.commons.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DeliveryView extends MenuPlayer {

  private BukkitTask task;

  public DeliveryView(Player player) {
    super(player, "Entregas", 5);

    task = new BukkitRunnable() {
      @Override
      public void run() {
      }
    }.runTaskTimer(Main.getInstance(), 0, 20);
  }

  @Override
  public void onClick(InventoryClickEvent evt) {

  }

  @Override
  public void onClose() {
    task.cancel();
    task = null;
    HandlerList.unregisterAll(this);
  }
}
