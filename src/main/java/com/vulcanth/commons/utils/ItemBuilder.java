package org.nebula.core.bukkit.utility;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.nebula.core.reflection.Accessors;
import org.nebula.core.reflection.MinecraftReflection;
import org.nebula.core.reflection.acessors.FieldAccessor;
import org.nebula.core.reflection.acessors.MethodAccessor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Onyzn
 */
public class ItemBuilder {

  private static final MethodAccessor GET_PROFILE;
  private static final FieldAccessor<GameProfile> SKULL_META_PROFILE;

  static {
    GET_PROFILE = Accessors.getMethod(MinecraftReflection.getCraftBukkitClass("entity.CraftPlayer"), GameProfile.class, 0);
    SKULL_META_PROFILE = Accessors.getField(MinecraftReflection.getCraftBukkitClass("inventory.CraftMetaSkull"), "profile", GameProfile.class);
  }

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

  public ItemBuilder skin(String texture) {
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", texture));
    SKULL_META_PROFILE.set(itemMeta, profile);
//    Field field = itemMeta.getClass().getDeclaredField("profile");
//    field.setAccessible(true);
//    field.set(itemMeta, profile);
    return this;
  }

  public ItemBuilder skin(Player player) {
    SKULL_META_PROFILE.set(itemMeta, (GameProfile) GET_PROFILE.invoke(player));
//    Field field = itemMeta.getClass().getDeclaredField("profile");
//    field.setAccessible(true);
//    field.set(itemMeta, ((CraftPlayer) player).getProfile());
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
}