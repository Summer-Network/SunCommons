package com.vulcanth.commons.bungee.commands.collections;

import com.vulcanth.commons.bungee.commands.CommandsAbstract;
import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import com.vulcanth.commons.bungee.proxied.cache.collections.PlayerInformationsCache;
import com.vulcanth.commons.bungee.proxied.role.ProxiedRole;
import com.vulcanth.commons.bungee.proxied.role.ProxiedRoleEnum;
import com.vulcanth.commons.storage.Database;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class TestandoCommand extends CommandsAbstract {

    public TestandoCommand() {
        super("rule", false);
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = getPlayerSender(sender);
            String options = args[0];
            switch (options.toLowerCase()) {
                case "info": {
                    if (args.length < 2) {
                        sender.sendMessage(TextComponent.fromLegacyText("§cUtilize \"/rule info <jogador>\" para ver o cargo atual do jogador na database."));
                        return;
                    }
                    String players = args[1];
                    ProxiedProfile profile = ProxiedProfile.loadProfile(players);
                    ProxiedRoleEnum role = ProxiedRole.findRoleByID(profile.getCache(PlayerInformationsCache.class).getInformation("role"));
                    player.sendMessage(TextComponent.fromLegacyText("§cO usuário " + profile.getName() + " atualmente está com o cargo: " + role));
                    break;
                }
                case "set": {
                    if (args.length < 3) {
                        sender.sendMessage(TextComponent.fromLegacyText("§cUtilize \"/rule set <jogador> [ID]\" para setar um registro na database."));
                        return;
                    }
                    String players = args[1];
                    ProxiedProfile profile = ProxiedProfile.loadProfile(players);
                    String currentInformations = (String) Database.getMySQL().getValue("VulcanthProfiles", "INFORMATIONS", "NAME", "=", profile.getName());
                    long amount = 0L;
                    try {
                        amount = Long.parseLong(args[2]);
                    } catch (Exception e) {
                        sender.sendMessage(TextComponent.fromLegacyText("§cTente utilizar números válidos."));
                    }

                    if (amount < 0) {
                        sender.sendMessage(TextComponent.fromLegacyText("§cVocê deve utilizar números acima ou igual a 0."));
                        return;
                    }
                    if (amount > 11) {
                        sender.sendMessage(TextComponent.fromLegacyText("§cVocê deve utilizar números abaixo ou igual a 11."));
                        return;
                    }
                    try {
                        JSONParser parser = new JSONParser();
                        JSONObject json = (JSONObject) parser.parse(currentInformations);

                        if (json.containsKey("role")) {
                            json.put("role", amount);

                            String updatedInformations = json.toJSONString();

                            Database.getMySQL().update("VulcanthProfiles", "INFORMATIONS", "=", "NAME", profile.getName(), updatedInformations);
                        }
                        sender.sendMessage(TextComponent.fromLegacyText("§aRegistro de " + profile.getName() + " alterado com sucesso."));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "delete": {
                    if (args.length < 2) {
                        sender.sendMessage(TextComponent.fromLegacyText("§cUtilize \"/rule delete <jogador>\" para excluir um registro na database."));
                        return;
                    }
                    String players = args[1];
                    ProxiedProfile profile = ProxiedProfile.loadProfile(players);
                    try {
                        Database.getMySQL().update("VulcanthProfiles", "INFORMATIONS", "=", "NAME", profile.getName(), null);
                        sender.sendMessage(TextComponent.fromLegacyText("§aRegistro de " + profile.getName() + " excluído com sucesso."));
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(TextComponent.fromLegacyText("§cOcorreu um erro ao excluir o registro de " + profile.getName()));
                    }
                    break;
                }
                default: {
                    sender.sendMessage(TextComponent.fromLegacyText("§cFaça alguma coisa, zé."));
                    break;
                }
            }
        }
    }
}
