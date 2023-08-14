package com.vulcanth.commons.view;

import com.vulcanth.commons.library.menu.MenuPlayer;
import com.vulcanth.commons.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ProfileView extends MenuPlayer {

  public ProfileView(Player player) {
    super(player, "Perfil", 3);

    setItem(10, ItemBuilder.of(Material.SKULL_ITEM).durability((short) 3).skin(player)
        .display("§aInformações pessoais")
        .lore("§fCargo: §dGay")
        .build()
    );

    setItem(11, ItemBuilder.of(Material.REDSTONE_COMPARATOR)
        .display("§aPreferências")
        .lore("§7ainda temos que kibar...")
        .build()
    );

    setItem(12, ItemBuilder.of(Material.PAPER)
        .display("§aEstatísticas")
        .lore("§7ainda temos que kibar...")
        .build()
    );

    setItem(14, ItemBuilder.of(Material.MINECART)
        .display("§aEntregas")
        .lore("§7ainda temos que kibar...")
        .build()
    );

    open();
  }

  @Override
  public void onClick(InventoryClickEvent evt) {
    switch (evt.getSlot()) {
      case 10:
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);
        break;
      case 11:

        break;
    }
  }

  @Override
  public void onClose() {
    HandlerList.unregisterAll(this);
  }
}
