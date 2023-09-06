package com.vulcanth.commons.plugin;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.utils.DiscordWebHook;
import com.vulcanth.commons.utils.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class VulcanthExeption {

    private final String code = StringUtils.getRandomCode(6);
    private final String cause;
    public VulcanthExeption(Player player, String throwable) {
        this.cause = throwable;
        player.sendMessage("§cOpa, ocorreu um erro ao executar este comando. " +
                "Você pode nos ajudar a resolver essse problema reportando-o em nosso fórum " +
                "e informando o ID §b#" + this.code + " §coara que possamos verificar o que ocorreu.");
        sendDiscordError();
    }

    public VulcanthExeption(CommandSender sender, String throwable) {
        this.cause = throwable;
        sender.sendMessage("§cOpa, ocorreu um erro ao executar este comando. " +
                "Você pode nos ajudar a resolver essse problema reportando-o em nosso fórum " +
                "e informando o ID §b#" + this.code + " §coara que possamos verificar o que ocorreu.");
        sendDiscordError();
    }

    public VulcanthExeption(net.md_5.bungee.api.CommandSender sender, String throwable) {
        this.cause = throwable;
        sender.sendMessage(TextComponent.fromLegacyText("§cOpa, ocorreu um erro ao executar este comando. " +
                "Você pode nos ajudar a resolver essse problema reportando-o em nosso fórum " +
                "e informando o ID §b#" + this.code + " §coara que possamos verificar o que ocorreu."));
        sendDiscordError();
    }

    public void sendDiscordError() {
        DiscordWebHook dc = new DiscordWebHook(Main.getInstance().getVulcanthConfig().findYamlByFileLink("config.yml").getStringWithColor("discordWebHook"));
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
