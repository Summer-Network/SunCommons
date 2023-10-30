package com.vulcanth.commons.model;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.vulcanth.commons.library.InvalidMojangException;
import com.vulcanth.commons.library.MojangAPI;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.collections.PlayerSkinCache;
import com.vulcanth.commons.utils.BukkitUtils;
import com.vulcanth.commons.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Skin {

    public static List<Skin> listSkins(Profile profile) {
        return profile.getCache(PlayerSkinCache.class).listSkinsUsed().stream().map(s -> new Skin(s.split(" ; ")[0], Long.parseLong(s.split(" ; ")[1]))).collect(Collectors.toList());
    }

    private final String name;
    private final Long lastUse;
    private final String signature;
    private final String value;

    public Skin(String name, long lastUse) {
        this.name = name;
        this.lastUse = lastUse;

        String skinInfo;
        try {
            skinInfo = MojangAPI.getSkinProperty(MojangAPI.getUUID(name));
        } catch (InvalidMojangException e) {
            skinInfo = "textures : ewogICJ0aW1lc3RhbXAiIDogMTYxMjU1OTc5ODMwOCwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZiOTAzMjBjN2FlOGFmNjE0ZTFmNmRiM2Y5MDdmNDBmNjdlNjY4MGUxZWI4N2U5MDNlYmI5YjI2Zjk0YzljMjQiCiAgICB9CiAgfQp9 : LjxNQagEnWdrNSVk9bM0iR/Sa/3xNOZwTxH61Ky5DSa+fRHmXvA8bYRu5usLIRWr9O89ObC3kiOLy8MbQFikERcXqgpZMgHh0GGQW+SEo4JyjI3iE3fT5v2YV1JFeSmGBYRy0v38osV+JfLapps39PKddwkY++19IlDWUQqskyDVyin2JdcNK7naeFxubxEX6R4QLqa5NHOdLU6Qdmw+i8kwJXw7ah/s8RhvBQlP/vm3URrunG9SvcYNfzw+c/7mHmKECe89xNjGpM9PvCFP8iZJd5XJIwLqBltuZaVHDyL7sMMBuZ853qjGt7cvGqqyDBgrdvkbKFCdV2GHIUE4pj6lbDmli/BBQtOP4GCkg/XSZB/J9gu00VgmVFJYSntHqRoHhh1rFM01hm91UpNzHOG13rqUXGdTl8QxxMaj1205h6JKRjNFJqDjJFJ/VmQ9zBMMDRZhRPFMwCnXYriJJbsaWVpKxs26Krz5oRIufqNdbhDH0BCwsA2b1gjPNBPl3hPcH5oVb8hYy9G9dbpnSIP5dbGCHHFX65idP2Dq3HvKA9gO5rWhqY+UVdlduzaOJwFLUPiVL4nTpeLmP+ya+LYYnkhY4FrjFAEzaVBpnWil+gCyz0radMpon1wrV7ytBFQR9fj1piYCMvvhgVTGCPAMPgXf1hME7LgX2G1ofyQ=";
        }

        this.value = skinInfo.split(" : ")[1];
        this.signature = skinInfo.split(" : ")[2];
    }

    public ItemStack buildSkinIcon(Profile profile) {
        if (profile.getCache(PlayerSkinCache.class).getSkinSelected().equals(this.name)) {
            return BukkitUtils.getItemStackFromString("SKULL_ITEM:3 : 1 : nome>&6" + this.name + " : desc>§fUsada pela última vez em: §7" + StringUtils.transformTimeFormated((double) (System.currentTimeMillis() - this.lastUse) / 1000) + "atrás\n \n§6Skin selecionada! : skin>" + this.value);
        }

        return BukkitUtils.getItemStackFromString("SKULL_ITEM:3 : 1 : nome>&a" + this.name + " : desc>§fUsada pela última vez em: §7" + StringUtils.transformTimeFormated((double) (System.currentTimeMillis() - this.lastUse) / 1000) + "atrás\n \n§eClique shift + clique direito para deletar!\n§aClique para utilizar essa skin! : skin>" + this.value);
    }

    public GameProfile buildGameProfile() {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "NYEL-RANDOLA");
        gameProfile.getProperties().clear();
        gameProfile.getProperties().put("textures", new Property("textures", this.value, this.signature));

        return gameProfile;
    }

    public String getName() {
        return this.name;
    }

    public Long getLastUse() {
        return this.lastUse;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }
}
