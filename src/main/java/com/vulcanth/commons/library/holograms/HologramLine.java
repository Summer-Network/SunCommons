package com.vulcanth.commons.library.holograms;

import com.vulcanth.commons.nms.NMS;
import com.vulcanth.commons.nms.NmsManager;
import com.vulcanth.commons.nms.collections.IEntityWrapper;
import com.vulcanth.commons.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@Getter
public class HologramLine {

    private final Location location;
    private InteractionHandler interactionHandler;
    private String line;
    private final Hologram hologram;
    private IEntityWrapper armor, slime, item;

    public HologramLine(Hologram hologram, Location location, String line) {
        this.location = location;
        this.hologram = hologram;
        setLine(line);
    }

    public void spawn() {
        if (armor == null) {
            armor = NMS.createArmorStand(location, line, this);
            if (interactionHandler != null) {
                setInteractionHandler(interactionHandler);
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

    public void setInteractionHandler(InteractionHandler interactionHandler) {
        if (interactionHandler == null) {
            if (slime != null) {
                slime.killEntity();
                slime = null;
            }
            this.interactionHandler = null;
            return;
        }

        if (armor != null) {
            slime = slime == null ? NmsManager.createSlime(location, this) : slime;

            if (slime != null) {
                slime.setPassengerOf(armor.getEntity());
            }

            this.interactionHandler = interactionHandler;
        }
    }

    public void setItem(ItemStack item) {
        if (item == null) {
            if (this.item != null) {
                this.item.killEntity();
                this.item = null;
            }
            return;
        }

        if (armor != null) {
            this.item = this.item == null ? NmsManager.createItem(location, item, this) : this.item;

            if (this.item != null) {
                this.item.setPassengerOf(armor.getEntity());
            }
        }
    }

    public void setLocation(Location location) {
        if (armor != null) {
            armor.setLocation(location);
            if (slime != null) {
                slime.setPassengerOf(armor.getEntity());
            }
        }
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
}
