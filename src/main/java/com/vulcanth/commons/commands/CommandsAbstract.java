package com.vulcanth.commons.commands;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.listeners.ListenersAbstract;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

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
            // TODO: fazer o sistema de ID
            commandSender.sendMessage("§cOpa, ocorreu um erro ao executar este comando. " +
                "Você pode nos ajudar a resolver essse problema reportando-o em nosso fórum " +
                "e informando o ID §b#000000 §coara que possamos verificar o que ocorreu.");
            ex.printStackTrace();
        }
        return true;
    }

    public abstract void executeCommand(CommandSender sender, String label, String[] args);

    public Player getPlayerSender(CommandSender sender) {
        return (Player) sender;
    }
}