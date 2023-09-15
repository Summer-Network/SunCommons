package com.vulcanth.commons.bungee.plugin;

import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import com.vulcanth.commons.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.IOException;

public class VulcanthBungeeException {

    private final String code = StringUtils.getRandomCode(6);
    private final String cause;
    private final String webhookURL = "https://discord.com/api/webhooks/1147334310173618236/YHcVJj0w-QfUJ35Hb-HXWliE5cR2x4TMvsOyB6aHDOxEY-oR8joBDJIiraF1M9FLhrgI";
    DiscordWebHook webhook = new DiscordWebHook(webhookURL);
    public VulcanthBungeeException(ProxiedProfile player, String throwable) {
        this.cause = throwable;
        player.getPlayer().sendMessage(TextComponent.fromLegacyText("§cOpa, ocorreu um erro ao executar este comando. " +
                "Você pode nos ajudar a resolver essse problema reportando-o em nosso fórum " +
                "e informando o ID §b#" + this.code + " §cpara que possamos verificar o que ocorreu."));
        sendDiscordError();
    }


    public VulcanthBungeeException(CommandSender sender, String throwable) {
        this.cause = throwable;
        sender.sendMessage(TextComponent.fromLegacyText("§cOpa, ocorreu um erro ao executar este comando. " +
                "Você pode nos ajudar a resolver essse problema reportando-o em nosso fórum " +
                "e informando o ID §b#" + this.code + " §cpara que possamos verificar o que ocorreu."));
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
