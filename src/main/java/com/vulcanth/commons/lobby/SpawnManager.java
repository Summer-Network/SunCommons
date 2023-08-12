package com.vulcanth.commons.lobby;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnManager {

    private static Location spawnLocation;

    public static void setSpawnLocation(Location location) {
        spawnLocation = location;
        Main.getInstance().getVulcanthConfig().findYamlByFileLink("config.yml").getYamlConfig().set("spawn-location", BukkitUtils.saveLocationForString(location));
        Main.getInstance().getVulcanthConfig().findYamlByFileLink("config.yml").save();
    }

    public static void setupLocation() {
        if (Main.getInstance().getVulcanthConfig().findYamlByFileLink("config.yml").getStringWithColor("spawn-location").isEmpty()) {
            spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
        } else {
            spawnLocation = BukkitUtils.loadLocationForString(Main.getInstance().getVulcanthConfig().findYamlByFileLink("config.yml").getStringWithColor("spawn-location"));
        }
    }

    public static Location getSpawnLocation() {
        return spawnLocation;
    }
}
