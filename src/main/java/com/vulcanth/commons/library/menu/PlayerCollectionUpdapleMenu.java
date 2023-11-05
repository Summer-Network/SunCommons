package com.vulcanth.commons.library.menu;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.library.MenuAbstract;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public abstract class PlayerCollectionUpdapleMenu extends MenuAbstract {

    private final Player player;
    private final List<Integer> onlySlots = new ArrayList<>();
    private final Map<Integer, Map<ItemStack, Object>> itensForSlot = new HashMap<>();
    private Integer delay;
    private BukkitTask task;
    private int nextItemSlot = 0;
    private int previusItemSlot = 0;
    private ItemStack nextItem = null;
    private ItemStack previusItem = null;
    private final List<ItemStack> itens = new ArrayList<>();
    private Map<ItemStack, Object> allItens;
    private int lastI;

    public PlayerCollectionUpdapleMenu(Player player, Integer slots) {
        super(slots);
        this.player = player;
    }

    public PlayerCollectionUpdapleMenu(Player player, Integer slots, String title) {
        super(slots, title);
        this.player = player;
    }

    public void open() {
        this.lastI = 0;
        this.player.openInventory(this.getInventory());
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                update(player);
                updateItens();
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0L, delay);
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
        if (this.itens.isEmpty()) {
            this.itens.addAll(itens.keySet());
        } else {
            this.itens.clear();
            this.itens.addAll(itens.keySet());
        }

        this.allItens = itens;
    }

    private void checkIfContinue(Map<ItemStack, Object> itens, int i) {
        if (this.onlySlots.size() > i) {
            Map<ItemStack, Object> itemValue = new HashMap<>();
            int slot = this.onlySlots.get(i);
            ItemStack item = this.itens.get(i + lastI);
            ItemStack itemPlaced = this.getInventory().getItem(slot);
            if (itemPlaced == null || itemPlaced.getType().equals(Material.AIR) || isDifferent(itemPlaced, item)) {
                this.setItem(item, slot);
            }

            itemValue.put(item, itens.get(item));
            itensForSlot.put(slot, itemValue);
            if (i == this.onlySlots.size() - 1 || i + this.lastI == this.itens.size() - 1) {
                this.lastI = (this.lastI + i) + 1;
            }
        }
    }

    private void updateItens() {
        this.itensForSlot.clear();
        if (this.onlySlots.size() >= this.itens.size()) {
            this.lastI = 0;
        } else {
            this.lastI = this.lastI - this.onlySlots.size();
        }

        for (int i = this.onlySlots.size() >= this.itens.size() ? 0 : this.lastI - onlySlots.size(); i + this.lastI < itens.size(); i++) {
            checkIfContinue(allItens, i);
        }

        if (this.lastI < itens.size() - 1) {
            this.setItem(this.nextItem, this.nextItemSlot);
        }

        clearItens();
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

    public BukkitTask getThread() {
        return task;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
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
            if (!this.onlySlots.contains(onlySlot)){
                this.setItem(new ItemStack(Material.AIR), onlySlot);
            }
        }

        if (this.lastI - 1 >= itens.size()) {
            this.setItem(new ItemStack(Material.AIR), this.nextItemSlot);
        }

        if (this.lastI - this.onlySlots.size() == 0) {
            this.setItem(new ItemStack(Material.AIR), this.previusItemSlot);
        }
    }

    public int getNextPageSlot() {
        return this.nextItemSlot;
    }

    public int getPreviusPageSlot() {
        return this.previusItemSlot;
    }

    private boolean isDifferent(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null || item1.getType().equals(Material.AIR) || item2.getType().equals(Material.AIR) || !item1.getItemMeta().equals(item2.getItemMeta())) {
            return true;
        }

        return !item1.getType().equals(item2.getType()) || !Objects.requireNonNull(item1.getItemMeta()).getDisplayName().equals(Objects.requireNonNull(item2.getItemMeta()).getDisplayName()) || !Objects.equals(Objects.requireNonNull(item1.getItemMeta()).getLore(), Objects.requireNonNull(item2.getItemMeta()).getLore());
    }

    public abstract void onPlayerCloseMenu(InventoryCloseEvent event);
    public abstract void onPlayerInteractMenu(InventoryClickEvent event);
    public abstract void onPlayerQuitOnMenu(PlayerQuitEvent event);
    public abstract void setupItens(Player player);
    public abstract void destroy();
    public abstract void update(Player player);
}
