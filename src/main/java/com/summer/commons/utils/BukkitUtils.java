package com.summer.commons.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class BukkitUtils {

    public static ItemStack getItemStackFromString(String itemStackString) {
        String[] itemFormat = itemStackString.replace("\\n", "\n").split(" : ");
        ItemStack finalItem;
        if (itemFormat[0].split(":").length > 1) {
            finalItem = new ItemStack(Material.matchMaterial(itemFormat[0].split(":")[0]), Integer.parseInt(itemFormat[1]));
            finalItem.setDurability(Short.parseShort(itemFormat[0].split(":")[1]));
        } else {
            finalItem = new ItemStack(Material.matchMaterial(itemFormat[0]), Integer.parseInt(itemFormat[1]));
        }

        ItemMeta itemMeta = finalItem.getItemMeta();
        List<String> lore = new ArrayList<>();
        Map<Enchantment, Integer> enchants = new HashMap<>();
        for (int i = 2; i < Arrays.stream(itemFormat).count(); i++) {
            String option = itemFormat[i];
            if (option.startsWith("nome>")) {
                itemMeta.setDisplayName(StringUtils.formatColors(itemFormat[i].split(">")[1]));
                finalItem.setItemMeta(itemMeta);
            } else if (option.startsWith("desc>")) {
                String finalDesc = option.split(">")[1];
                String[] ac = finalDesc.split("\n");
                for (int o = 0; o < Arrays.stream(ac).count(); o++) {
                    lore.add(StringUtils.formatColors(ac[o]));
                }
                itemMeta.setLore(lore);
            } else if (option.startsWith("encantar>")) {
                for (String enchant : option.split(">")[1].split("\n")) {
                    enchants.put(Enchantment.getByName(enchant.split(":")[0]), Integer.valueOf(enchant.split(":")[1]));
                }
                finalItem.addEnchantments(enchants);
            }  else if (option.startsWith("esconder>")) {
                String[] flags = option.split(">")[1].split("\n");
                for (String flag : flags) {
                    if (flag.equalsIgnoreCase("tudo")) {
                        itemMeta.addItemFlags(ItemFlag.values());
                        break;
                    } else {
                        itemMeta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
                    }
                }
            } else if (option.startsWith("dono>")) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;
                try {
                    Player player = Bukkit.getPlayer(option.split(">")[1]);
                    GameProfile profile = ((CraftPlayer) player).getProfile();
                    Field profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, profile);
                } catch (Exception e) {
                    skullMeta.setOwner(option.split(">")[1]);
                }
            } else if (option.startsWith("skin>")) {
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "PERSONALITED");
                gameProfile.getProperties().put("textures", new Property("textures", option.split(">")[1]));
                SkullMeta skullMeta = (SkullMeta) itemMeta;
                try {
                    Field profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, gameProfile);
                } catch (IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchFieldException e) {e.printStackTrace();}
            } else if (option.startsWith("pattern>")) {
                String colorPattern = option.split(">")[1].split(":")[0];
                String patternType = option.split(">")[1].split(":")[1];
                BannerMeta bannerMeta = (BannerMeta) itemMeta;
                bannerMeta.addPattern(new Pattern(DyeColor.valueOf(colorPattern.toUpperCase()), PatternType.valueOf(patternType.toUpperCase())));
            } else if (option.startsWith("bannerBaseColor>")) {
                BannerMeta bannerMeta = (BannerMeta) itemMeta;
                bannerMeta.setBaseColor(DyeColor.valueOf(option.split(">")[1].toUpperCase()));
            } else if (option.startsWith("paint>")) {
                LeatherArmorMeta meta = (LeatherArmorMeta) itemMeta;
                int r = Integer.parseInt(option.split(">")[1].split(":")[0]);
                int g = Integer.parseInt(option.split(">")[1].split(":")[1]);
                int b = Integer.parseInt(option.split(">")[1].split(":")[2]);
                meta.setColor(Color.fromRGB(r, g, b));
            }

            finalItem.setItemMeta(itemMeta);
        }
        return finalItem;
    }

    public static ItemStack putGlowInItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static String saveLocationForString(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float pitch = location.getPitch();
        float yaw = location.getYaw();
        String worldName = location.getWorld().getName();

        return x + ";" + y + ";" + z + ";" + worldName + ";" + yaw + ";" + pitch;
    }

    public static Location loadLocationForString(String locationString) {
        String[] locationOptions = locationString.split(";");
        if (locationOptions.length == 6) {
            try {
                double x = Double.parseDouble(locationOptions[0]);
                double y = Double.parseDouble(locationOptions[1]);
                double z = Double.parseDouble(locationOptions[2]);
                World world = Bukkit.getWorld(locationOptions[3]);
                float yaw = Float.parseFloat(locationOptions[4]);
                float pitch = Float.parseFloat(locationOptions[5]);

                return new Location(world, x, y, z, yaw , pitch);
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }
}
