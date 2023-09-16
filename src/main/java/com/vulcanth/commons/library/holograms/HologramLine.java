package com.vulcanth.commons.library.holograms;

import com.vulcanth.commons.nms.NMS;
import com.vulcanth.commons.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class HologramLine {

    private final Location location;
    private IArmorStand armor;
    private ISlime slime;
    private IItem item;
    private TouchHandler touch;
    private PickupHandler pickup;
    private String line;
    private final Hologram hologram;

    public HologramLine(Hologram hologram, Location location, String line) {
        this.location = location;
        this.hologram = hologram;
        setLine(line);
    }

    public void spawn() {
        if (armor == null) {
            armor = NMS.createArmorStand(location, line, this);
            if (touch != null) {
                setTouchable(touch);
            }
        }
    }

    public void despawn() {
        if (armor != null) {
            armor.killEntity();
            armor = null;
        }
        if (slime != null) {
            slime.killEntity();
            slime = null;
        }
        if (item != null) {
            item.killEntity();
            item = null;
        }
    }

    public void setTouchable(TouchHandler touch) {
        if (touch == null) {
            if (slime != null) {
                slime.killEntity();
                slime = null;
            }
            this.touch = null;
            return;
        }

        if (armor != null) {
            slime = slime == null ? NMS.createSlime(location, this) : slime;

            if (slime != null) {
                slime.setPassengerOf(armor.getEntity());
            }

            this.touch = touch;
        }
    }

    public void setItem(ItemStack item, PickupHandler pickup) {
        if (pickup == null) {
            if (this.item != null) {
                this.item.killEntity();
                this.item = null;
            }
            this.pickup = null;
            return;
        }

        if (armor != null) {
            this.item = this.item == null ? NMS.createItem(location, item, this) : this.item;

            if (this.item != null) {
                this.item.setPassengerOf(armor.getEntity());
            }

            this.pickup = pickup;
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (armor != null) {
            armor.setLocation(location.getX(), location.getY(), location.getZ());
            if (slime != null) {
                slime.setPassengerOf(armor.getEntity());
            }
        }
    }

    public IArmorStand getArmor() {
        return armor;
    }

    public ISlime getSlime() {
        return slime;
    }

    public TouchHandler getTouchHandler() {
        return touch;
    }

    public PickupHandler getPickupHandler() {
        return pickup;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        String formattedLine = StringUtils.formatColors(line);
        if (!formattedLine.equals(this.line)) {
            this.line = formattedLine;
            if (armor == null && hologram.isSpawned()) {
                spawn();
            } else if (armor != null) {
                armor.setName(this.line);
            }
        }
    }

    public Hologram getHologram() {
        return hologram;
    }
}
