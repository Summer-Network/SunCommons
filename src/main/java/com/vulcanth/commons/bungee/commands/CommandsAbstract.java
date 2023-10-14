package com.vulcanth.commons.bungee.commands;

import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.bungee.plugin.VulcanthBungeeException;
import com.vulcanth.commons.plugin.VulcanthExeption;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

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
        super(command, null, aliases);
        this.onlyPlayer = onlyPlayer;

        BungeeMain.getInstance().getProxy().getPluginManager().registerCommand(BungeeMain.getInstance(), this);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (onlyPlayer) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(TextComponent.fromLegacyText("§cComando de uso exclusivo para jogadores!"));
                return;
            }
        }

        try {
            this.executeCommand(sender, args);
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stack : ex.getStackTrace()) {
                sb.append(stack.toString()).append(" ");
            }

            new VulcanthBungeeException(sender, sb.toString());
        }
    }

    public abstract void executeCommand(CommandSender sender, String[] args);

    public ProxiedPlayer getPlayerSender(CommandSender sender) {
        return (ProxiedPlayer) sender;
    }
}