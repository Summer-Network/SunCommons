package com.vulcanth.commons.view;

import com.vulcanth.commons.library.menu.PlayerUpdapleMenu;
import com.vulcanth.commons.model.Delivery;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.collections.PlayerDeliveryCache;
import com.vulcanth.commons.utils.BukkitUtils;
import com.vulcanth.commons.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

public class DeliveryMenu extends PlayerUpdapleMenu {

    private Profile profile;

    public DeliveryMenu(Profile profile) {
        super(profile.getPlayer(), 6, "Entregas");
        this.profile = profile;

        this.setDelay(10);
        this.registerMenu();
        setupItens(profile.getPlayer());
    }

    @Override
    public void onPlayerCloseMenu(InventoryCloseEvent event) {
        if (event.getInventory().equals(this.getInventory())) {
            destroy();
        }
    }

    @Override
    public void onPlayerInteractMenu(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (player == this.profile.getPlayer()) {
                if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR) && player.getOpenInventory().getTopInventory().equals(this.getInventory())) {
                    int slot = event.getSlot();
                    PlayerDeliveryCache cache = profile.getCache(PlayerDeliveryCache.class);
                    new DeliveryMenu(profile);
                    event.setCancelled(true);
                    if (slot == 49) {
                        new ProfileMenu(profile).open();
                        player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 2.0F);
                    } else {
                        Arrays.stream(Delivery.values()).filter(delivery -> delivery.getSlot() == slot).findFirst().ifPresent(delivery -> {
                            if (!delivery.canCollect(player)) {
                                return;
                            }

                            if (cache.hasColletedDelivery(delivery.getId())) {
                                player.sendMessage("\n§7Você poderá coletar novamente em §f" + StringUtils.transformTimeFormated(cache.getInSecounds(delivery.getId())) + "§7.");
                                return;
                            }

                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.5F, 1.0F);
                            cache.setDeliveryClaim(delivery.getId(), System.currentTimeMillis(), delivery.getDays());
                            delivery.setupReward(profile);
                            player.sendMessage("§aRecompensa coletada com sucesso!");
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerQuitOnMenu(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (this.profile.getPlayer() == player) {
            if (player.getOpenInventory().getTopInventory().equals(this.getInventory())) {
                destroy();
            }
        }
    }

    @Override
    public void setupItens(Player player) {
        for (Delivery delivery : Delivery.values()) {
            this.setItem(delivery.getItem(profile), delivery.getSlot());
        }

        this.setItem(BukkitUtils.getItemStackFromString("ARROW : 1 : nome>&cVoltar : desc>&7Clique para voltar para o menu anterior"), 49);
    }

    @Override
    public void destroy() {
        this.profile = null;
        this.getThread().cancel();
        HandlerList.unregisterAll(this);
    }


    @Override
    public void update(Player player) {
        for (Delivery delivery : Delivery.values()) {
            this.setItem(delivery.getItem(profile), delivery.getSlot());
        }
    }
}
