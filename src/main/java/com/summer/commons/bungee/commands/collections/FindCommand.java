package com.summer.commons.bungee.commands.collections;

import com.summer.commons.bungee.commands.CommandsAbstract;
import com.summer.commons.bungee.proxied.ProxiedProfile;
import com.summer.commons.bungee.proxied.cache.collections.PlayerInformationsCache;
import com.summer.commons.bungee.proxied.role.ProxiedRole;
import com.summer.commons.bungee.proxied.role.ProxiedRoleEnum;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FindCommand extends CommandsAbstract {
    public FindCommand() {
        super("find", true);
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        ProxiedPlayer player = getPlayerSender(sender);
        if (ProxiedRole.findRole(getPlayerSender(sender)).getId() > 3) {
            sender.sendMessage(TextComponent.fromLegacyText("§fComando desconhecido."));
            return;
        }
        ProxiedProfile profile = ProxiedProfile.loadProfile(player.getName());
        if (args.length < 1) {
            player.sendMessage(TextComponent.fromLegacyText("§cUtilize \"/find [PLAYER]\"."));
            return;
        }
        ProxiedPlayer play = ProxyServer.getInstance().getPlayer(args[0]);
        ProxiedProfile profi = ProxiedProfile.loadProfile(play.getName());
        ProxiedRoleEnum roled = ProxiedRole.findRole(profi.getPlayer());
        String serverName = play.getServer().getInfo().getName();
        String formattedServerName = formatServerName(serverName);
        if (profi != null) {
            player.sendMessage(TextComponent.fromLegacyText(
                    "\n§cPerfil:" +
                            " \n §fNick§7: " + play.getName() +
                            " \n §fUUID§7: " + "§cem breve" + " §e(§aem breve§e)" +
                            " \n §fCadastrado em§7: " + profi.getCache(PlayerInformationsCache.class).getInformation("firstLogin") +
                            " \n §fPrimeiro login§7: " + profi.getCache(PlayerInformationsCache.class).getInformation("firstLogin") +
                            " \n §fÚltimo login§7: " + profi.getCache(PlayerInformationsCache.class).getInformation("lastLogin") +
                            " \n §fÚltimo IP§7: " + (ProxiedRole.findRole(getPlayerSender(sender)).getId() > 2 ? "§cSem permissão" : profi.getPlayer().getAddress().getHostString()) +
                            " \n §fCash§7: " + profi.getCache(PlayerInformationsCache.class).getInformation("cash") +
                            " \n §fServidor§7: " + (play.getName() != null ? formattedServerName : "Jogador offline") +
                            //" \n §fContas§7: " + nLoginAPI.getApi().getAccountsByIp(target.getAddress().getAddress().getHostAddress()).stream().map(account -> account.getRealName()).collect(Collectors.toList()) +
                            " \n §fPing§7: " + (play.getName() != null ? play.getPing() : "§cJogador offline") +
                            "\n§cGrupos:" +
                            " \n §fPrefixos§7: " + roled.getName() + "\n"));

        } else {
            player.sendMessage(TextComponent.fromLegacyText("§cEste jogador não existe."));
        }
    }
    public static String formatServerName(String input) {
        if (input.startsWith("skywars-lobby")) {
            return "Lobby Sky Wars " + input.substring(input.length() - 1); // pega o último char, que deverá ser um número
        } else if (input.startsWith("sw-solo")) {
            return "Sky Wars Solo";
        } else if (input.startsWith("sw-duo")) {
            return "Sky Wars Dupla";
        } else if (input.startsWith("lobby")) {
            return "Lobby principal " + input.substring(input.length() - 1);
        }
        return "Autenticação";
    }
}
