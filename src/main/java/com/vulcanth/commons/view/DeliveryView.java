package com.vulcanth.commons.view;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.library.menu.MenuPlayer;
import com.vulcanth.commons.model.delivery.Delivery;
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
        for (Delivery delivery : Delivery.DELIVERIES) {
          String color = "§a";
          String click = "§cVocê não tem permissão.";
          if (!delivery.hasPermission(player)) {
            color = "§c";
            click = "§eClique para coletar!";
          }

          setItem(delivery.getSlot(), ItemBuilder.cloneOf(delivery.getIcon())
              .displayPrefix(color).appendLore("", click).build()
          );
        }
      }
    }.runTaskTimer(Main.getInstance(), 0, 20);
  }

  @Override
  public void onClick(InventoryClickEvent evt) {
    Delivery delivery = Delivery.getBySlot(evt.getSlot());

    if (delivery == null) return;

    // fazer verificações antes de poder coletar as recompensas
    // ^^^

    delivery.getRewards().forEach(r -> r.dispath(player));
  }

  @Override
  public void onClose() {
    task.cancel();
    task = null;
    HandlerList.unregisterAll(this);
  }
}
