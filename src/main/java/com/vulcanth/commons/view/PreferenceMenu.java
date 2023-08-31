package com.vulcanth.commons.view;

import com.vulcanth.commons.library.menu.PlayerMenu;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.collections.PlayerInformationsCache;
import com.vulcanth.commons.player.cache.collections.PlayerPreferencesCache;
import com.vulcanth.commons.player.preferences.PreferencesEnum;
import com.vulcanth.commons.player.role.Role;
import com.vulcanth.commons.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PreferenceMenu extends PlayerMenu {

    private Profile profile;

    public PreferenceMenu(Profile profile) {
        super(profile.getPlayer(), 6, "Preferências");
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
                    PlayerPreferencesCache cache = this.profile.getCache(PlayerPreferencesCache.class);
                    switch (slot) {
                        case 0: {
                            player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 2.0F);
                            break;
                        }

                        case 9: {
                            cache.changePreference(PreferencesEnum.SHOW_PLAYERS);
                            this.profile.refreshHotbar();
                            this.profile.refreshPlayers();
                            new PreferenceMenu(profile).open();
                            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5F, 1.0F);
                            break;
                        }

                        case 1: {
                            player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 2.0F);
                            break;
                        }

                        case 10: {
                            cache.changePreference(PreferencesEnum.PRIVATE_MESSAGES);
                            new PreferenceMenu(profile).open();
                            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5F, 1.0F);
                            break;
                        }

                        case 2: {
                            player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 2.0F);
                            break;
                        }

                        case 11: {
                            cache.changePreference(PreferencesEnum.BLOOD);
                            new PreferenceMenu(profile).open();
                            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5F, 1.0F);
                            break;
                        }

                        case 3: {
                            player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 2.0F);
                            break;
                        }

                        case 12: {
                            cache.changePreference(PreferencesEnum.LOBBY_PROTECTION);
                            new PreferenceMenu(profile).open();
                            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5F, 1.0F);
                            break;
                        }

                        case 49: {
                            new ProfileMenu(profile).open();
                            player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 2.0F);
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
        PlayerPreferencesCache cache = this.profile.getCache(PlayerPreferencesCache.class);

        //Player show
        this.setItem(BukkitUtils.getItemStackFromString("347 : 1 : nome>&aJogadores : desc>&7Ative ou desative os\n&7jogadores no lobby."), 0);
        this.setItem(BukkitUtils.getItemStackFromString("160:" + cache.getGlassColor(PreferencesEnum.SHOW_PLAYERS) + " : 1 : nome>" + cache.getStateWithColor(PreferencesEnum.SHOW_PLAYERS) + " : desc>&fEstado: &7" + cache.getState(PreferencesEnum.SHOW_PLAYERS) + "\n \n&eClique para modificar!"), 9);

        //Private messages
        this.setItem(BukkitUtils.getItemStackFromString("PAPER : 1 : nome>&aMensagens Privadas : desc>&7Ative ou desative as mensagens\n&7enviadas através de um tell."), 1);
        this.setItem(BukkitUtils.getItemStackFromString("160:" + cache.getGlassColor(PreferencesEnum.PRIVATE_MESSAGES) + " : 1 : nome>" + cache.getStateWithColor(PreferencesEnum.PRIVATE_MESSAGES) + " : desc>&fEstado: &7" + cache.getState(PreferencesEnum.SHOW_PLAYERS) + "\n \n&eClique para modificar!"), 10);

        //Blood
        this.setItem(BukkitUtils.getItemStackFromString("331 : 1 : nome>&aViolência : desc>&7Ative ou desative as partículas\n&7de sangue no PvP."), 2);
        this.setItem(BukkitUtils.getItemStackFromString("160:" + cache.getGlassColor(PreferencesEnum.BLOOD) + " : 1 : nome>" + cache.getStateWithColor(PreferencesEnum.BLOOD) + " : desc>&fEstado: &7" + cache.getState(PreferencesEnum.BLOOD) + "\n \n&eClique para modificar!"), 11);

        //Lobby Protection
        this.setItem(BukkitUtils.getItemStackFromString("399 : 1 : nome>&aProteção no /lobby : desc>&7Ative ou desative o pedido\n&7de confirmação ao utilizar o /lobby"), 3);
        this.setItem(BukkitUtils.getItemStackFromString("160:" + cache.getGlassColor(PreferencesEnum.LOBBY_PROTECTION) + " : 1 : nome>" + cache.getStateWithColor(PreferencesEnum.LOBBY_PROTECTION) + " : desc>&fEstado: &7" + cache.getState(PreferencesEnum.BLOOD) + "\n \n&eClique para modificar!"), 12);

        this.setItem(BukkitUtils.getItemStackFromString("ARROW : 1 : nome>&cVoltar : desc>&7Clique para voltar para o menu anterior"), 49);
    }

    @Override
    public void destroy() {
        this.profile = null;
        HandlerList.unregisterAll(this);
    }
}
