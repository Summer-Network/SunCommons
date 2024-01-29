package com.summer.commons.commands.collections;

import com.summer.commons.commands.CommandsAbstract;
import com.summer.commons.player.Profile;
import com.summer.commons.player.role.Role;
import com.summer.commons.player.role.RoleEnum;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoarCommand extends CommandsAbstract {

    public VoarCommand() {
        super("voar", true, "fly");
    }

    @Override
    public void executeCommand(CommandSender sender, String label, String[] args) {
        Player player = getPlayerSender(sender);
        Profile profile = Profile.loadProfile(player.getName());
        if (profile != null) {
            RoleEnum role = Role.findRole(player);
            if (profile.getGame() != null) {
                player.sendMessage("§cSó é possível utilizar este comando no lobby!");
                return;
            }

            if (!role.canFly()) {
                player.sendMessage("§cSomente VIP ou superior podem executar este comando.");
                return;
            }

            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.sendMessage("§eModo voar desativado!");
            } else {
                player.setAllowFlight(true);
                player.setFlying(true);
                player.sendMessage("§eModo voar ativado!");
            }
        }
    }
}
