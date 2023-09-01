package com.vulcanth.commons.commands.collections;

import com.vulcanth.commons.commands.CommandsAbstract;
import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cash.CashManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CashCommand extends CommandsAbstract {

    public CashCommand() {
        super("cash", false);
    }

    @Override
    public void executeCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("vulcanthcommons.cmd.cash") || args.length < 1) {
                sender.sendMessage("§eSeu cash: §b" + new CashManager(Objects.requireNonNull(Profile.loadProfile(sender.getName()))).getCash());
                return;
            }
        } else {
            if (args.length < 1) {
                sender.sendMessage("§cArgumentos insuficientes!");
                return;
            }
        }

        String options = args[0];
        switch (options.toLowerCase()) {
            case "add": {
                if (args.length < 3) {
                    sender.sendMessage("§cTente /cash add [jogador] [quantia].");
                    return;
                }

                Player targetP = null;
                try {
                    targetP = Bukkit.getPlayer(args[1]);
                } catch (Exception e) {
                    sender.sendMessage("§cJogador offline no momento.");
                }

                long amount = 0L;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (Exception e) {
                    sender.sendMessage("§cTente utilizar números válidos.");
                }

                if (amount <= 0) {
                    sender.sendMessage("§cVocê deve utilizar números acima de 0.");
                    return;
                }

                new CashManager(Objects.requireNonNull(Profile.loadProfile(targetP.getName()))).addCash(amount);
                sender.sendMessage("§aQuantia de cash setada com sucesso!");
                break;
            }

            case "remove": {
                if (args.length < 3) {
                    sender.sendMessage("§cTente /cash remove [jogador] [quantia].");
                    return;
                }

                Player targetP = null;
                try {
                    targetP = Bukkit.getPlayer(args[0]);
                } catch (Exception e) {
                    sender.sendMessage("§cJogador offline no momento.");
                }

                long amount = 0L;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (Exception e) {
                    sender.sendMessage("§cTente utilizar números válidos.");
                }

                if (amount <= 0) {
                    sender.sendMessage("§cVocê deve utilizar números acima de 0.");
                    return;
                }

                CashManager cashManager = new CashManager(Objects.requireNonNull(Profile.loadProfile(targetP.getName())));
                if (cashManager.getCash() - amount <= 0) {
                    sender.sendMessage("§aNão é possível deixar o jogador zerado ou com cash negativo!");
                    return;
                }

                cashManager.removeCash(amount);
                sender.sendMessage("§aQuantia de cash removida com sucesso!");
                break;
            }

            case "set": {
                if (args.length < 3) {
                    sender.sendMessage("§cTente /cash set [jogador] [quantia].");
                    return;
                }

                Player targetP = null;
                try {
                    targetP = Bukkit.getPlayer(args[0]);
                } catch (Exception e) {
                    sender.sendMessage("§cJogador offline no momento.");
                }

                long amount = 0L;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (Exception e) {
                    sender.sendMessage("§cTente utilizar números válidos.");
                }

                if (amount <= 0) {
                    sender.sendMessage("§cVocê deve utilizar números acima de 0.");
                    return;
                }

                CashManager cashManager = new CashManager(Objects.requireNonNull(Profile.loadProfile(targetP.getName())));
                cashManager.setCash(amount);
                sender.sendMessage("§aQuantia de cash setada com sucesso!");
                break;
            }

            default: {
                if (sender instanceof Player) {
                    sender.sendMessage("§eSeu cash: §b" + new CashManager(Objects.requireNonNull(Profile.loadProfile(sender.getName()))).getCash());
                } else {
                    sender.sendMessage("§cArgumentos insuficientes!");
                }
                break;
            }
        }
    }
}
