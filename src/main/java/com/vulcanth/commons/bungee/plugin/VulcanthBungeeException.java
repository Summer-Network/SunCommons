package com.vulcanth.commons.bungee.plugin;

import com.vulcanth.commons.bungee.BungeeMain;
import com.vulcanth.commons.bungee.proxied.ProxiedProfile;
import com.vulcanth.commons.utils.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.IOException;

public class VulcanthBungeeException {

    private final String code = StringUtils.getRandomCode(6);
    private final String cause;
    public VulcanthBungeeException(ProxiedProfile player, String throwable) {
        this.cause = throwable;
        player.getPlayer().sendMessage(TextComponent.fromLegacyText("§cOpa, ocorreu um erro ao executar este comando. " +
                "Você pode nos ajudar a resolver essse problema reportando-o em nosso fórum " +
                "e informando o ID §b#" + this.code + " §coara que possamos verificar o que ocorreu."));
        sendDiscordError();
    }


    public VulcanthBungeeException(net.md_5.bungee.api.CommandSender sender, String throwable) {
        this.cause = throwable;
        sender.sendMessage(TextComponent.fromLegacyText("§cOpa, ocorreu um erro ao executar este comando. " +
                "Você pode nos ajudar a resolver essse problema reportando-o em nosso fórum " +
                "e informando o ID §b#" + this.code + " §coara que possamos verificar o que ocorreu."));
        sendDiscordError();
    }

    public void sendDiscordError() {
        DiscordWebHook dc = new DiscordWebHook(BungeeMain.getInstance().getConfig().getString("discordWebHook"));
        DiscordWebHook.EmbedObject embed = new DiscordWebHook.EmbedObject();
        dc.setContent("<@&1147354520758788158>");
        embed.addField("Código:", this.code, false);
        embed.addField("Erro:", this.cause, false);
        dc.addEmbed(embed);
        try {
            dc.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
