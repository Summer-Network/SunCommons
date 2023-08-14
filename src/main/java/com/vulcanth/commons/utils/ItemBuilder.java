package com.vulcanth.commons.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.SneakyThrows;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Onyzn
 */
public class ItemBuilder {

  private final ItemStack itemStack;
  private final ItemMeta itemMeta;

  public ItemBuilder(ItemStack itemStack) {
    this.itemStack = itemStack;
    this.itemMeta = itemStack.getItemMeta();
  }

  public ItemBuilder material(Material material) {
    itemStack.setType(material);
    return this;
  }

  public ItemBuilder amount(int amount) {
    itemStack.setAmount(amount);
    return this;
  }

  public ItemBuilder durability(short durability) {
    itemStack.setDurability(durability);
    return this;
  }

  public ItemBuilder enchantment(Enchantment enchantment, int level) {
    itemStack.addUnsafeEnchantment(enchantment, level);
    return this;
  }

  public ItemBuilder effect(PotionEffect effect, boolean overwrite) {
    ((PotionMeta) itemMeta).addCustomEffect(effect, overwrite);
    return this;
  }

  public ItemBuilder display(String display) {
    itemMeta.setDisplayName(display);
    return this;
  }

  public ItemBuilder displayPrefix(String displayPrefix) {
    itemMeta.setDisplayName(displayPrefix + itemMeta.getDisplayName());
    return this;
  }

  public ItemBuilder displaySuffix(String displaySuffix) {
    itemMeta.setDisplayName(itemMeta.getDisplayName() + displaySuffix);
    return this;
  }

  public ItemBuilder lore(String... lines) {
    itemMeta.setLore(Arrays.asList(lines));
    return this;
  }

  public ItemBuilder appendLore(String... lines) {
    List<String> lore = itemMeta.getLore();
    lore.addAll(Arrays.asList(lines));
    itemMeta.setLore(lore);
    return this;
  }

  public ItemBuilder flags(ItemFlag... flags) {
    itemMeta.addItemFlags(flags);
    return this;
  }

  public ItemBuilder flagsAll() {
    itemMeta.addItemFlags(ItemFlag.values());
    return this;
  }

  public ItemBuilder owner(String owner) {
    ((SkullMeta) itemMeta).setOwner(owner);
    return this;
  }

  public ItemBuilder unbreakable(boolean unbreakable) {
    itemMeta.spigot().setUnbreakable(unbreakable);
    return this;
  }

  @SneakyThrows
  public ItemBuilder skin(String texture) {
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", texture));
    Field field = itemMeta.getClass().getDeclaredField("profile");
    field.setAccessible(true);
    field.set(itemMeta, profile);
    return this;
  }

  @SneakyThrows
  public ItemBuilder skin(Player player) {
    Field field = itemMeta.getClass().getDeclaredField("profile");
    field.setAccessible(true);
    field.set(itemMeta, ((CraftPlayer) player).getProfile());
    return this;
  }

  public ItemBuilder leatherColor(int r, int g, int b) {
    ((LeatherArmorMeta) itemMeta).setColor(Color.fromRGB(r, g, b));
    return this;
  }

  protected ItemBuilder meta(Consumer<ItemMeta> consumer) {
    consumer.accept(itemMeta);
    return this;
  }

  public ItemStack build() {
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

  public static ItemBuilder of(Material material) {
    return new ItemBuilder(new ItemStack(material));
  }

  public static ItemBuilder of(ItemStack itemStack) {
    return new ItemBuilder(itemStack);
  }

  public static ItemBuilder cloneOf(ItemStack itemStack) {
    return new ItemBuilder(itemStack.clone());
  }

  public static ItemBuilder headOf(Player player) {
    return of(Material.SKULL_ITEM).durability((short) 3).skin(player);
  }

  public static ItemBuilder headOf(String texture) {
    return of(Material.SKULL_ITEM).durability((short) 3).skin(texture);
  }
}