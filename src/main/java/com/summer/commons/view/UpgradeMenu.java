package com.summer.commons.view;

import com.summer.commons.model.UpgradeVIP;
import com.summer.commons.Main;
import com.summer.commons.library.menu.PlayerMenu;
import com.summer.commons.nms.NmsManager;
import com.summer.commons.player.Profile;
import com.summer.commons.player.cash.CashManager;
import com.summer.commons.player.role.Role;
import com.summer.commons.player.role.RoleEnum;
import com.summer.commons.utils.StringUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UpgradeMenu extends PlayerMenu {

    private Profile profile;

    public UpgradeMenu(Profile profile) {
        super(profile.getPlayer(), 3, "Upgrade de VIP");
        this.profile = profile;

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
                    if (slot == 13) {
                        RoleEnum role = Role.findRole(player);
                        UpgradeVIP upgrade = UpgradeVIP.findUpgradeVIP(role);
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5F, 1.0F);
                        player.sendMessage("§eAtivando produto...");
                        new CashManager(profile).removeCash(upgrade.getPrice());
                        player.closeInventory();
                        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
                            if (player.isOnline()) {
                                player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.5F, 0.5F);
                                player.sendMessage("\n§aSeu vip " + role.getName() + " §afoi desativado.\n \n§aSeu VIP " + upgrade.getRole().getName() + " §afoi ativo! Caso seu grupo ainda não tenha sido\n§aatualizado, basta relogar para que tudo funcione\n§acorrentamente. Agradecemos pela confiança e tenha um bom §ajogo!\n");
                                TextComponent component = new TextComponent("");
                                player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.5F, 1.0F);
                                for (BaseComponent baseComponent : TextComponent.fromLegacyText("§a§lYAY! §aSua compra foi aprovada!\n \n§7Agora você possui o VIP " + StringUtils.formatColors(upgrade.getRole().getName()) + "\n \n§7Caso você precise de\n§7ajuda entre em\n§7contato, através de\n§7nosso suporte\n§7clicando ")) {
                                    component.addExtra(baseComponent);
                                }

                                TextComponent urlDiscord = new TextComponent("§0§laqui§7!");
                                urlDiscord.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://vulcanth.com/forum/"));
                                urlDiscord.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Clique para abrir")));
                                component.addExtra(urlDiscord);
                                player.spigot().sendMessage(component);
                            }

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent set " + upgrade.getRole().getGroupName());

                            for (Player playerLobby : Bukkit.getOnlinePlayers()) {
                                playerLobby.playSound(playerLobby.getLocation(), Sound.ENDERDRAGON_GROWL, 0.5F, 0.5F);
                                NmsManager.sendTitle(playerLobby, upgrade.getRole().getColor() + player.getName(), "§ftornou-se " + upgrade.getRole().getName(), 10, 20, 10);
                            }
                        }, 20 * 5);
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
        UpgradeVIP upgrade = UpgradeVIP.findUpgradeVIP(Role.findRole(player));
        this.setItem(upgrade.getIcon(), 13);
    }

    @Override
    public void destroy() {
        this.profile = null;
        HandlerList.unregisterAll(this);
    }
}