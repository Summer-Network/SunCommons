package com.vulcanth.commons.bungee.commands.collections;

import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.bungee.commands.CommandsAbstract;
import com.vulcanth.commons.bungee.proxied.role.ProxiedRole;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;

public class ManutencaoCommand extends CommandsAbstract {

    public ManutencaoCommand() {
        super("manutencao", false);
    }

    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = getPlayerSender(sender);
            if (!(ProxiedRole.findRole(player).getId() >= 1)) {
                player.sendMessage(TextComponent.fromLegacyText("§cSomente gerente ou superior podem executar este comando."));
                return;
            }
        }

        changeMaintance(sender);
    }

    public void changeMaintance(CommandSender sender) {
        if (BungeeMain.isIsMaintence()) {
            BungeeMain.setIsMaintence(false);
            sender.sendMessage(TextComponent.fromLegacyText("§cManutenção desativada com sucesso!"));
            Objects.requireNonNull(BungeeMain.getYaml("options.yml", "plugins/" + BungeeMain.getInstance().getDescription().getName())).set("manutencao", false);
        } else {
            BungeeMain.setIsMaintence(true);
            sender.sendMessage(TextComponent.fromLegacyText("§aManutenção ativada com sucesso!"));
            Objects.requireNonNull(BungeeMain.getYaml("options.yml", "plugins/" + BungeeMain.getInstance().getDescription().getName())).set("manutencao", true);
        }
    }

}
