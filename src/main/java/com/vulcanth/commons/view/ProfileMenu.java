package com.vulcanth.commons.view;

import com.vulcanth.commons.library.menu.PlayerMenu;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.collections.PlayerInformationsCache;
import com.vulcanth.commons.player.cash.CashManager;
import com.vulcanth.commons.player.role.Role;
import com.vulcanth.commons.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileMenu extends PlayerMenu {

    private Profile profile;

    public ProfileMenu(Profile profile) {
        super(profile.getPlayer(), 3, "Meu Perfil");
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

                        case 10: {
                            new PreferenceMenu(profile).open();
                            player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 2.0F);
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
        this.setItem(BukkitUtils.getItemStackFromString("397:3 : 1 : nome>&aInformações pessoais : desc>&fRank: " + Role.findRole(player).getName() + "\n&fCash: &6" + new CashManager(profile).getCash() + "\n \n&fCadastrado: &7" + profile.getCache(PlayerInformationsCache.class).getInformation("firstLogin") + "\n&fPrimeiro Login: &7" + profile.getCache(PlayerInformationsCache.class).getInformation("firstLogin") + "\n&fÚltimo acesso: &7" + profile.getCache(PlayerInformationsCache.class).getInformation("lastLogin") + "\n \n&fCelular: &cEm Breve\n&fDiscord: " + discord + "\n&fEmail: " + email + " : skin>" + player.getName()), 13);

        //Preferences
        this.setItem(BukkitUtils.getItemStackFromString("404 : 1 : nome>&aPreferências : desc>&7Em nosso servidor você pode personalizar\n&7sua experiência de jogo por completo.\n&7Personalize várias opções únicas como\n&7você desejar!\n \n&8As opções incluem ativar ou desativar as\n&8mensagens privadas, os jogadores e outros.\n \n&eClique para personalizar as opções!"), 10);

        //Statistics
        this.setItem(BukkitUtils.getItemStackFromString("PAPER : 1 : nome>&aEstatísticas : desc>&7Visualize as suas estatísticas de\n&7cada minigame do nosso servidor.\n \n&eClique para ver as estatísticas!"), 11);

        boolean has = false;
        //Delivery
        this.setItem(BukkitUtils.getItemStackFromString(has ? "342 : 1 : nome>&aEntregas : desc>&7Colete mensalmente itens exclusivos.\n&7Atualmente, você pode coletar &n" + "entregas.size()" + " entregas!\n" + "stringBuilder" + "\n \n&eClique para ver!" : "328 : 1 : nome>&aEntregas : desc>&7Você não possui entregas para coletar."), 15);

        //Upgrade Vip
        this.setItem(BukkitUtils.getItemStackFromString("384 : 1 : nome>&aEvolua seu VIP : desc>" + (has ? "\n§7Você já possui " + Role.findRole(player.getPlayer()).getName() + "§7.\n \n§7Evolua agora mesmo o seu " + "Role.getPlayerRole(player.getPlayer()).getName()" + "\n§7para " + "Role.getRoleByName(new UpgradePlayer(player).getCurrentUpgrade().getRole()).getName()" + "§7 por apenas §6" + "new UpgradePlayer(player).getCurrentUpgrade().getPrice()" + " §6Cash§7.\n\n§eClique para upar seu VIP." : "§7Você não possui evoluções disponíveis.")), 16);
    }

    @Override
    public void destroy() {
        this.profile = null;
        HandlerList.unregisterAll(this);
    }
}
