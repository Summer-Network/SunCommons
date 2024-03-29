package com.summer.commons.view;

import com.summer.commons.model.Delivery;
import com.summer.commons.model.UpgradeVIP;
import com.summer.commons.library.menu.PlayerMenu;
import com.summer.commons.player.Profile;
import com.summer.commons.player.cache.collections.PlayerDeliveryCache;
import com.summer.commons.player.cache.collections.PlayerInformationsCache;
import com.summer.commons.player.cash.CashManager;
import com.summer.commons.player.role.Role;
import com.summer.commons.utils.BukkitUtils;
import com.summer.commons.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

public class ProfileMenu extends PlayerMenu {

    private Profile profile;

    public ProfileMenu(Profile profile) {
        super(profile.getPlayer(), 5, "Meu Perfil");
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
                    switch (slot) {
                        case 13: {
                            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5F, 1.0F);
                            break;
                        }

                        case 29: {
                            new PreferenceMenu(profile).open();
                            player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 2.0F);
                            break;
                        }

                        case 31: {
                            new DeliveryMenu(profile).open();
                            player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 2.0F);
                            break;
                        }

                        case 32: {
                            UpgradeVIP upgrade = UpgradeVIP.findUpgradeVIP(Role.findRole(player));
                            if (upgrade != null) {
                                long cash = upgrade.getPrice();
                                if (new CashManager(profile).getCash() < cash) {
                                    TextComponent component = new TextComponent("");
                                    for (BaseComponent components : TextComponent.fromLegacyText("\n§cAparentemente você não possui cash o suficiente para upar o seu vip, para adquirir mais, basta clicar ")) {
                                        component.addExtra(components);
                                    }
                                    getLink(component, player);
                                    return;
                                }

                                player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 1.0F);
                                new UpgradeMenu(profile).open();
                            } else {
                                TextComponent component = new TextComponent("");
                                for (BaseComponent components : TextComponent.fromLegacyText("\n§cAparentemente você não possui um vip para upa-lo! Para comprar um, clique ")) {
                                    component.addExtra(components);
                                }
                                getLink(component, player);
                            }
                            break;
                        }
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
        //Head informations
        String discord = profile.getCache(PlayerInformationsCache.class).getInformation("discord").equals("") ? "&cNão cadastrado" : profile.getCache(PlayerInformationsCache.class).getInformation("discord");
        String email = profile.getCache(PlayerInformationsCache.class).getInformation("email").equals("") ? "&cNão cadastrado" : profile.getCache(PlayerInformationsCache.class).getInformation("email");
        this.setItem(BukkitUtils.getItemStackFromString("397:3 : 1 : nome>&aInformações pessoais : desc>&fRank: " + Role.findRole(player).getName() + "\n&fCash: &6" + new CashManager(profile).getCash() + "\n \n&fCadastrado: &7" + profile.getCache(PlayerInformationsCache.class).getInformation("firstLogin") + "\n&fPrimeiro Login: &7" + profile.getCache(PlayerInformationsCache.class).getInformation("firstLogin") + "\n&fÚltimo acesso: &7" + profile.getCache(PlayerInformationsCache.class).getInformation("lastLogin") + "\n \n&fCelular: &cEm Breve\n&fDiscord: " + discord + "\n&fEmail: " + email + " : dono>" + player.getName()), 13);

        //Preferences
        this.setItem(BukkitUtils.getItemStackFromString("404 : 1 : nome>&aPreferências : desc>&7Em nosso servidor você pode personalizar\n&7sua experiência de jogo por completo.\n&7Personalize várias opções únicas como\n&7você desejar!\n \n&8As opções incluem ativar ou desativar as\n&8mensagens privadas, os jogadores e outros.\n \n&eClique para personalizar as opções!"), 29);

        //Statistics
        this.setItem(BukkitUtils.getItemStackFromString("PAPER : 1 : nome>&aEstatísticas : desc>&7Visualize as suas estatísticas de\n&7cada minigame do nosso servidor.\n \n&eClique para ver as estatísticas!"), 30);

        boolean has;
        //Delivery
        PlayerDeliveryCache cache = profile.getCache(PlayerDeliveryCache.class);
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(Delivery.values()).filter(delivery -> delivery.canCollect(player) && !cache.hasColletedDelivery(delivery.getId())).forEach(delivery -> stringBuilder.append("\n &7• &f").append(StringUtils.stripColors(delivery.getItem(profile).getItemMeta().getDisplayName())));
        has = Delivery.listAllDeliveryNotClaim(profile).size() > 1;
        this.setItem(BukkitUtils.getItemStackFromString(has ? "342 : 1 : nome>&aEntregas : desc>&7Colete mensalmente itens exclusivos.\n&7Atualmente, você pode coletar &n" + Delivery.listAllDeliveryNotClaim(profile).size() + " entregas!\n" + stringBuilder + "\n \n&eClique para ver!" : "328 : 1 : nome>&aEntregas : desc>&7Você não possui entregas para coletar."), 31);

        //Upgrade Vip
        has = UpgradeVIP.findUpgradeVIP(Role.findRole(player)) != null;
        this.setItem(BukkitUtils.getItemStackFromString("384 : 1 : nome>&aEvolua seu VIP : desc>" + (has ? "\n§7Você já possui " + Role.findRole(player.getPlayer()).getName() + "§7.\n \n§7Evolua agora mesmo o seu " + Role.findRole(player.getPlayer()).getName() + "\n§7para " + UpgradeVIP.findUpgradeVIP(Role.findRole(player)).getRole().getName() + "§7 por apenas §6" + UpgradeVIP.findUpgradeVIP(Role.findRole(player)).getPrice() + " §6Cash§7.\n\n§eClique para upar seu VIP." : "§7Você não possui evoluções disponíveis.")), 32);

        this.setItem(BukkitUtils.getItemStackFromString("389 : 1 : nome>&aSua skin : desc>&7Altere a sua skin atual.\n \n&eClique para saber mais!"), 33);
    }

    @Override
    public void destroy() {
        this.profile = null;
        HandlerList.unregisterAll(this);
    }

    private void getLink(TextComponent component, Player player) {
        TextComponent click = new TextComponent("AQUI");
        click.setColor(ChatColor.GOLD);
        click.setBold(true);
        click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://redesummer.com.br"));
        click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Clique para ir a nossa loja.")));
        component.addExtra(click);
        for (BaseComponent components : TextComponent.fromLegacyText(" §cpara ir a nossa loja.\n")) {
            component.addExtra(components);
        }
        player.spigot().sendMessage(component);
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 0.5F, 0.5F);
        player.closeInventory();
    }
}
