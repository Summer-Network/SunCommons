package com.summer.commons.view;

import com.summer.commons.library.menu.PlayerUpdapleMenu;
import com.summer.commons.model.Server;
import com.summer.commons.player.Profile;
import com.summer.commons.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerMenu extends PlayerUpdapleMenu {

    private Profile profile;

    public ServerMenu(Profile profile) {
        super(profile.getPlayer(), 3, "Modos de jogo");
        this.profile = profile;

        this.setDelay(10);
        setupItens(profile.getPlayer());
        registerMenu();
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
                    Server server = Server.findBySlot(slot);
                    if (server != null) {
                        server.connect(profile);
                    }

                    event.setCancelled(true);
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
        this.setItem(BukkitUtils.getItemStackFromString("DIAMOND : 1 : nome>&bContribua com a Rede Summer : desc>  \n &7Você pode ajudar a Summer  \n &7tornando-se &fVIP &7e adquirindo  \n &7pacotes de &fCash&7!  \n \n &7Além de ajudar a manter a rede \n &7online, você ainda terá acesso  \n &7a vários &fbenefícios&7! \n \n§eCLique para ir a loja!"), 11);
        for (Server server : Server.values()) {
            this.setItem(server.getIcon(), server.getSlot());
        }
    }

    @Override
    public void destroy() {
        this.profile = null;
        HandlerList.unregisterAll(this);
    }

    @Override
    public void update(Player player) {
        for (Server server : Server.values()) {
            this.setItem(server.getIcon(), server.getSlot());
        }
    }
}
