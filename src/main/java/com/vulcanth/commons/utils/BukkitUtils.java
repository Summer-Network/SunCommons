package com.vulcanth.commons.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class BukkitUtils {

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
