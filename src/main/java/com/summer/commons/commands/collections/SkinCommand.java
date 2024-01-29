package com.summer.commons.commands.collections;

import com.summer.commons.view.SkinMenu;
import com.summer.commons.commands.CommandsAbstract;
import com.summer.commons.player.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkinCommand extends CommandsAbstract {

    public SkinCommand() {
        super("skin", true);
    }

    @Override
    public void executeCommand(CommandSender sender, String label, String[] args) {
        Player player = getPlayerSender(sender);
        Profile profile = Profile.loadProfile(player.getName());
        if (profile == null) {
            return;
        }

        if (args.length < 1) {
            new SkinMenu(profile).open();
            return;
        }

    }
}
