package com.vulcanth.commons.commands.collections;

import com.vulcanth.commons.commands.CommandsAbstract;
import com.vulcanth.commons.view.DeliveryView;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FireworkCommand extends CommandsAbstract {

  public FireworkCommand() {
    super("firework", false);
  }

  @Override
  public void executeCommand(CommandSender sender, String label, String[] args) {
    new DeliveryView((Player) sender);
  }
}
