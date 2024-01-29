package com.summer.commons.library.menu;

import com.summer.commons.library.MenuAbstract;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class PlayerCollectionMenu extends MenuAbstract {

    private final Player player;
    private final List<Integer> onlySlots = new ArrayList<>();
    private int nextItemSlot = 0;
    private int previusItemSlot = 0;
    private ItemStack nextItem = null;
    private ItemStack previusItem = null;
    private int lastI;
    private final Map<Integer, Map<ItemStack, Object>> itensForSlot = new HashMap<>();
    private final List<ItemStack> itens = new ArrayList<>();
    private Map<ItemStack, Object> allItens;

    public PlayerCollectionMenu(Player player, Integer slots) {
        super(slots);
        this.player = player;
    }

    public PlayerCollectionMenu(Player player, Integer slots, String title) {
        super(slots, title);
        this.player = player;
    }

    public void open() {
        this.player.openInventory(this.getInventory());
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        this.onPlayerCloseMenu(event);
    }

    @EventHandler
    public void onPlayerInteractInventory(InventoryClickEvent event) {
        this.onPlayerInteractMenu(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.onPlayerQuitOnMenu(event);
    }

    public void setItens(Map<ItemStack, Object> itens) {
        this.itens.addAll(itens.keySet());

        for (int i = 0; i < itens.size(); i++) {
            checkIfContinue(itens, i);
        }

        if (this.lastI < itens.size()) {
            this.setItem(this.nextItem, this.nextItemSlot);
        }

        this.allItens = itens;
    }

    private void checkIfContinue(Map<ItemStack, Object> itens, int i) {
        if (this.onlySlots.size() > i) {
            Map<ItemStack, Object> itemValue = new HashMap<>();
            int slot = this.onlySlots.get(i);
            ItemStack item = this.itens.get(i + lastI);
            this.setItem(item, slot);
            itemValue.put(item, itens.get(item));
            itensForSlot.put(slot, itemValue);
            if (i == this.onlySlots.size() - 1 || i + this.lastI == this.itens.size() - 1) {
                this.lastI = (this.lastI + i) + 1;
            }
        }
    }

    public void setOnlySlots(List<Integer> onlySlots) {
        this.onlySlots.addAll(onlySlots);
    }

    public void setOnlySlots(Integer... onlySlots) {
        this.onlySlots.addAll(Arrays.asList(onlySlots));
    }

    public ItemStack getItemForSlot(Integer slot) {
        return itensForSlot.get(slot).keySet().stream().findFirst().get();
    }

    public Object getValueForItem(Integer slot, ItemStack item) {
        return itensForSlot.get(slot).get(item);
    }

    public int getNextPageSlot() {
        return this.nextItemSlot;
    }

    public int getPreviusPageSlot() {
        return this.previusItemSlot;
    }

    public void setNextItemSlot(int slot) {
        this.nextItemSlot = slot;
    }

    public void setPreviusItemSlot(int slot) {
        this.previusItemSlot = slot;
    }

    public void setPreviusItem(ItemStack previusItem) {
        this.previusItem = previusItem;
    }

    public void setNextItem(ItemStack nextItem) {
        this.nextItem = nextItem;
    }

    public void nextPage() {
        clearItens();
        for (int i = 0; i + this.lastI < itens.size(); i++) {
            checkIfContinue(this.allItens, i);
        }

        if (this.lastI < itens.size()) {
            this.setItem(this.nextItem, this.nextItemSlot);
        }

        this.setItem(this.previusItem, this.previusItemSlot);
    }

    public void previusPage() {
        clearItens();
        this.lastI = (this.lastI - this.onlySlots.size()) - 1;
        for (int i = 0; i + this.lastI < itens.size(); i++) {
            checkIfContinue(this.allItens, i);
        }

        if (this.lastI - 1 < itens.size()) {
            this.setItem(this.nextItem, this.nextItemSlot);
        }

        if (this.lastI - this.onlySlots.size() != 0) {
            this.setItem(this.previusItem, this.previusItemSlot);
        }
    }

    private void clearItens() {
        for (Integer onlySlot : this.onlySlots) {
            this.setItem(new ItemStack(Material.AIR), onlySlot);
        }

        this.setItem(new ItemStack(Material.AIR), this.nextItemSlot);
        this.setItem(new ItemStack(Material.AIR), this.previusItemSlot);

        this.itensForSlot.clear();
    }

    public abstract void onPlayerCloseMenu(InventoryCloseEvent event);
    public abstract void onPlayerInteractMenu(InventoryClickEvent event);
    public abstract void onPlayerQuitOnMenu(PlayerQuitEvent event);
    public abstract void setupItens(Player player);
    public abstract void destroy();
}
