package com.vulcanth.commons.commands.collections;

import com.vulcanth.commons.commands.CommandsAbstract;
import com.vulcanth.commons.library.NPCManager;
import com.vulcanth.commons.library.npc.NPC;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCCommand extends CommandsAbstract {

    public NPCCommand() {
        super("npc", true);
    }

    @Override
    public void executeCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando só pode ser executado por jogadores.");
            return;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("§cUtilize \"/npc [create/remove] [nome do NPC]\".");
            return;
        }

        if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("criar")) {
            if (args.length < 3) {
                player.sendMessage("§cUtilize \"/npc create <id> [nome do NPC]\" para criar um NPC.");
                return;
            }
            String id = args[1];
            String npcName = args[2];

            if (npcName != null) {
                Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
                location.setYaw(player.getLocation().getYaw());
                location.setPitch(player.getLocation().getPitch());

                //NPC npc = NPCManager.createNPC(id, location, npcName);
                NPCManager.createNPC(id, location, npcName);
                player.sendMessage("§aNPC criado com sucesso: " + npcName);
            } else {
                player.sendMessage("§cEste NPC não existe");
            }

        } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("remover")) {
            if (args.length < 2) {
                player.sendMessage("§cUtilize \"/npc remove <ID>\" para remover um NPC.");
                return;
            }
            String id = args[1];
            NPC npc = NPCManager.findByID(id);
            if (npc != null) {
                NPCManager.removeNPC(npc);
                player.sendMessage("§cNPC removido com sucesso: " + id);
            } else {
                player.sendMessage("§cEste NPC não existe");
            }
        } else {
            player.sendMessage("§cUtilize \"/npc [create/remove] [nome do NPC]\".");
        }
    }
}
