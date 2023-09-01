package com.vulcanth.commons.commands;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.listeners.ListenersAbstract;
import com.vulcanth.commons.plugin.VulcanthExeption;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public abstract class CommandsAbstract extends Command {

    @SuppressWarnings("unchecked")
    public static void setupComands(Class<? extends CommandsAbstract>... commandsClasses) {
        for (Class<? extends CommandsAbstract> clazz : commandsClasses) {
            try {
                clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
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

        try {
            this.executeCommand(commandSender, s, strings);
        } catch (Exception ex) {
            new VulcanthExeption(commandSender, ex.getStackTrace()[0].toString());
        }

        return true;
    }

    public abstract void executeCommand(CommandSender sender, String label, String[] args);

    public Player getPlayerSender(CommandSender sender) {
        return (Player) sender;
    }
}