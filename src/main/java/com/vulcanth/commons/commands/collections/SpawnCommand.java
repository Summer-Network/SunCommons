package com.vulcanth.commons.commands.collections;

import com.vulcanth.commons.commands.CommandsAbstract;
import com.vulcanth.commons.lobby.SpawnManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends CommandsAbstract {

    public SpawnCommand() {
        super("setspawn", true);
    }

    @Override
    public void executeCommand(CommandSender sender, String label, String[] args) {
        Player player = getPlayerSender(sender);
        String a = args[1];
        if (!player.hasPermission("vulcanthcommons.cmd.setspawn")) {
            player.sendMessage("§cComando exclusivo para §4Gerente §cou superior!");
            return;
        }

        SpawnManager.setSpawnLocation(player.getLocation());
        player.sendMessage("§aSpawn setado com sucesso!");
    }

}
