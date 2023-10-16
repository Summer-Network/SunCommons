package com.vulcanth.commons.commands.collections;

import com.vulcanth.commons.commands.CommandsAbstract;
import com.vulcanth.commons.library.HologramManager;
import com.vulcanth.commons.library.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HologramCommand extends CommandsAbstract {

    public HologramCommand() {
        super("hologram", true);
    }

    @Override
    public void executeCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando só pode ser executado por jogadores.");
            return;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("§cUtilize \"/hologram [create/remove] [TEXTO]\".");
            return;
        }

        if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("criar")) {
            if (args.length < 3) {
                player.sendMessage("§cUtilize \"/hologram create <id> [TEXTO]\" para criar um holograma.");
                return;
            }
            String id = args[1];
            String holograma = args[2];

            if (id != null) {
                Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
                location.setYaw(player.getLocation().getYaw());
                location.setPitch(player.getLocation().getPitch());

                HologramManager.createHologram(id, location, holograma);

                player.sendMessage("§aHolograma criado com sucesso: " + id);
            } else {
                player.sendMessage("§cEste Holograma já existe");
            }

        } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("remover")) {
            if (args.length < 2) {
                player.sendMessage("§cUtilize \"/hologram remove <ID>\" para remover um holograma.");
                return;
            }
            String id = args[1];
            Hologram npc = HologramManager.findByID(id);
            if (npc != null) {
                HologramManager.removeNPC(npc);
                player.sendMessage("§cHolograma removido com sucesso: " + id);
            } else {
                player.sendMessage("§cEste holograma não existe");
            }
        } else {
            player.sendMessage("§cUtilize \"/hologram [create/remove] [TEXTO]\".");
        }
    }
}
