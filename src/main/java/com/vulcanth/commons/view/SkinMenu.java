package com.vulcanth.commons.view;

import com.vulcanth.commons.library.menu.PlayerCollectionMenu;
import com.vulcanth.commons.model.Server;
import com.vulcanth.commons.model.Skin;
import com.vulcanth.commons.model.SkinCacheCommand;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.collections.PlayerSkinCache;
import com.vulcanth.commons.utils.BukkitUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinMenu extends PlayerCollectionMenu {

    private Profile profile;

    public SkinMenu(Profile profile) {
        super(profile.getPlayer(), 6, "Modos de jogo");
        this.profile = profile;
        this.setOnlySlots(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37));
        this.setNextItem(BukkitUtils.getItemStackFromString("ARROW : 1 : nome>&aPróxima Página"));
        this.setNextItemSlot(35);
        this.setPreviusItem(BukkitUtils.getItemStackFromString("ARROW : 1 : nome>&aPágina anterior"));
        this.setPreviusItemSlot(27);

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
                        case 48: {
                            if (!player.hasPermission("skin.change")) {
                                player.sendMessage(" \n&eDesculpe você não possui permissão para alterar sua skin\n&bCompre VIPs nossa loja para desbloquear esta função!");
                                player.closeInventory();
                                return;
                            }

                            TextComponent principal = new TextComponent(" \n§aDigite o nick da skin que você deseja no chat\n§7Ou clique ");

                            TextComponent aqui = new TextComponent("AQUI");
                            aqui.setColor(ChatColor.GREEN);
                            aqui.setBold(true);
                            aqui.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "SKIN.CANCEL"));

                            principal.addExtra(aqui);
                            principal.addExtra(" §7para cancelar essa operação\n ");

                            player.spigot().sendMessage(principal);
                            player.closeInventory();
                            SkinCacheCommand.addSkinProgress(player.getName());
                            break;
                        }

                        case 49: {
                            profile.getCache(PlayerSkinCache.class).updateSkinSelected("");
                            player.sendMessage("§aSua skin foi atualizada com sucesso, relogue para visualisar.");
                            break;
                        }

                        case 50: {
                            player.closeInventory();
                            player.sendMessage(" \n§eAjuda - Skins\n \n§3/skin atualizar §f- §7Retomar a skin original da sua conta.\n§3/skin [jogador] §f- §7Utilizar a skin de outro jogador.\n ");
                            break;
                        }

                        default: {
                            Skin skin = (Skin) this.getValueForItem(slot, event.getCurrentItem());
                            if (skin != null) {
                                profile.getCache(PlayerSkinCache.class).updateSkinSelected(skin.getName());
                                profile.getCache(PlayerSkinCache.class).putSkinUse(skin.getName());
                                player.sendMessage("§aSua skin foi atualizada com sucesso, relogue para visualisar.");
                                new SkinMenu(profile).open();
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
        this.setItem(BukkitUtils.getItemStackFromString("BOOK_AND_QUILL : 1 : nome>§aEscolher : desc>§7Você pode escolher uma nova skin\n§7para ser utilizada em sua conta.\n \n§fComando: §7/skin [jogador]\n \n§eClique para escolher uma skin."), 48);
        this.setItem(BukkitUtils.getItemStackFromString("ITEM_FRAME : 1 : nome>§aAtualizar : desc>&7Altere a sua skin para a mais recente\n&7utilizada em sua conta original!\n \n&8Caso você utilize minecraft pirata\n&8sua skin será padronizada!\n \n&fComando:&7 /skin atualizar\n \n&eClique para atualizar sua skin!"), 49);
        this.setItem(BukkitUtils.getItemStackFromString("SKULL_ITEM:3 : 1 : nome>§aAjuda : desc>§7As ações disponíveis neste menu também\n§7podem ser realizadas por comando.\n \n§fComando: §7/skin ajuda\n \n§eClique para listar os comandos. : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19"), 50);

        List<Skin> skins = profile.getSkins();
        if (skins.isEmpty()) {
            this.setItem(BukkitUtils.getItemStackFromString("WEB : 1 : nome>§aVazio! : desc>§7Você não possui skins salvas no momento!"), 22);
        } else {
            Map<ItemStack, Object> actions = new HashMap<>();
            for (Skin skin : skins) {
                actions.put(skin.buildSkinIcon(this.profile), skin);
            }

            this.setItens(actions);
        }
    }

    @Override
    public void destroy() {
        this.profile = null;
        HandlerList.unregisterAll(this);
    }
}
