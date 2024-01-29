package com.summer.commons.plugin;

import com.summer.commons.utils.DiscordWebHook;
import com.summer.commons.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class VulcanthExeption {

    private final String code = StringUtils.getRandomCode(6);
    private final String cause;
    private final String webhookURL = "https://discord.com/api/webhooks/1147334310173618236/YHcVJj0w-QfUJ35Hb-HXWliE5cR2x4TMvsOyB6aHDOxEY-oR8joBDJIiraF1M9FLhrgI";
    DiscordWebHook webhook = new DiscordWebHook(webhookURL);

    public VulcanthExeption(Player player, String throwable) {
        this.cause = throwable;
        player.sendMessage("§cOpa, ocorreu um erro ao executar este comando. " +
                "Você pode nos ajudar a resolver essse problema reportando-o em nosso fórum " +
                "e informando o ID §b#" + this.code + " §cpara que possamos verificar o que ocorreu.");
        sendDiscordError();
    }

    public VulcanthExeption(CommandSender sender, String throwable) {
        this.cause = throwable;
        sender.sendMessage("§cOpa, ocorreu um erro ao executar este comando. " +
                "Você pode nos ajudar a resolver essse problema reportando-o em nosso fórum " +
                "e informando o ID §b#" + this.code + " §cpara que possamos verificar o que ocorreu.");
        sendDiscordError();
    }

    public void sendDiscordError() {

        String mentionMessage = "<@&1147354520758788158>";
        DiscordWebHook.EmbedObject embed = new DiscordWebHook.EmbedObject()
                .addField("Código:", this.code, false)
                .addField("Erro:", this.cause, false);

        try {
            webhook.setContent(mentionMessage);
            webhook.addEmbed(embed);
            webhook.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
