package org.nebula.core.bukkit.view;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.nebula.core.bukkit.library.menu.MenuPlayer;
import org.nebula.core.bukkit.utility.ItemBuilder;
import org.nebula.core.bukkit.utility.ComponentBuilder;

public class ProfileView extends MenuPlayer {

  public ProfileView(Player player) {
    super(player, "Perfil", 3);

    setItem(10, ItemBuilder.of(Material.SKULL_ITEM).durability((short) 3).skin(player)
        .display("§aInformações pessoais")
        .lore("§fCargo: §dGay")
        .build()
    );

    setItem(11, ItemBuilder.of(Material.LEATHER_CHESTPLATE).leatherColor(255, 0, 0)
        .display("§aAparência")
        .lore("§7Modifique a aparência do seu", "§7personagem.")
        .flagsAll()
        .build()
    );

    open();
  }

  @Override
  public void onClick(InventoryClickEvent evt) {
    switch (evt.getSlot()) {
      case 10:
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);

        new DeliveryView(player);
        break;
      case 11:
        player.spigot().sendMessage(ComponentBuilder
            .of("§cEssa funcionalidade está atualmente indisponível!")
            .tooltip("§4Em breve!\n§cTentaremos tornar esta funcionaidade disponível.")
            .build()
        );

        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
        break;
    }
  }

  @Override
  public void onClose() {

  }
}
