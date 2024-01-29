package com.summer.commons.commands.collections;

import com.summer.commons.lobby.SpawnManager;
import com.summer.commons.commands.CommandsAbstract;
import com.summer.commons.player.role.Role;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends CommandsAbstract {

    public SpawnCommand() {
        super("setspawn", true);
    }

    @Override
    public void executeCommand(CommandSender sender, String label, String[] args) {
        Player player = getPlayerSender(sender);
        if (Role.findRole(player).getId() > 1) {
            player.sendMessage("§fComando desconhecido.");
            return;
        }

        SpawnManager.setSpawnLocation(player.getLocation());
        player.sendMessage("§aSpawn setado com sucesso!");
    }

}
