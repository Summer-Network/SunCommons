package com.vulcanth.commons.commands;

import com.vulcanth.commons.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.util.Arrays;

public abstract class CommandsAbstract extends Command {

    public static void setupCommands() {
    }

    private final boolean onlyPlayer;

    public CommandsAbstract(String command, boolean onlyPlayer, String... aliases) {
        super(command);
        this.setAliases(Arrays.asList(aliases));
        this.onlyPlayer = onlyPlayer;

        try {
            SimpleCommandMap scm = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
            scm.register(Main.getInstance().getDescription().getName(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (onlyPlayer) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("§cComando de uso exclusivo para jogadores!");
                return false;
            }
        }

        this.executeCommand(commandSender, s, strings);
        return true;
    }

    public abstract void executeCommand(CommandSender sender, String label, String[] args);

    public Player getPlayerSender(CommandSender sender) {
        return (Player) sender;
    }
}