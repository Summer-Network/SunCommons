package com.summer.commons.model;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.summer.commons.Main;
import com.summer.commons.player.Profile;
import com.summer.commons.utils.BukkitUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum Server {

    LOBBY("47 : 1 : nome>&6Lobby Principal : desc>&eClique para conectar", 10, "ca-01.bed.ovh:25573:lobby"),
    SKYWARS("GRASS : 1 : nome>&aSky Wars : desc> \n &eConfira a atualização &n0.1\n &bredesummer.com.br/skywars\n \n &7Você tem medo de altura? Então este\n &7jogo não é para você! No Sky Wars, você\n &7deverá eliminar os seus adversários com\n &7e ajuda de diversos Kits e Habilidades.\n \n &8• &fSolo\n &8• &fDupla\n \n&aClique para conectar!\n&7{players} jogando.", 14, "ca-01.bed.ovh:6379:skywars-lobby-1");
//    BEDWARS("BED : 1 : nome>&aBed Wars &b&lv0.0 : desc>\n  &eConfira a atualização &n0.0&e:\n  &bvulcanth.com/bedwars\n \n  &7▪ &dReconexão\n  &7▪ &dOrganizador de Hotbar\n  &7▪ &dDistribuição igualitária de drops\n \n  &7Este jogo é de tirar o sono! O objetivo\n  &7é simples e curioso, destruir a cama de\n  &7seus inimigos e proteger a sua. \n  \n  &7▪ &fSolo\n  &7▪ &fDupla\n  &7▪ &fTrio\n  &7▪ &fQuarteto\n \n&aClique para conectar!\n&7{players} jogando.", 29, ""),
//    THE_BRIDGE("STAINED_CLAY:11 : 1 : nome>&aThe Bridge : desc>\n  &7Prepare-se para balancear muito bem sua defesa\n  &7com seu ataque! Neste minigame você deve ser o\n  &7mais rápido para alcançar a base do seu inimigo\n  &7e marcar um ponto antes dele!\n \n  &7▪ &f1v1\n  &7▪ &f2v2\n  &7▪ &f3v3\n  &7▪ &f4v4\n  &7▪ &f2v2v2v2\n  &7▪ &f3v3v3v3\n \n&aClique para conectar!\n&7{players} jogando.", 32, ""),
//    BUILD_BATTLE("58 : 1 : nome>&aBuild Battle : desc>\n  &7Neste minigame, você deverá construir\n  &7de acordo com o tema sorteado e, após\n  &7isso, avaliar a construção de seus\n  &7adversários.\n \n  &7▪ &fSolo\n  &7▪ &fDupla\n  &7▪ &fAdvinhe\n \n&aClique para conectar!\n&7{players} jogando.", 33, ""),
//    TEST_ZONE("137 : 1 : nome>&aZona de Teste : desc>\n  &7Neste lobby, você testará minigames em\n  &7desenvolvimento e que dependem do seu\n  &7deedback para tornarem-se um modo de\n  &7jogo completo no futuro.\n \n  &7▪ &fGladiador\n  &7▪ &fMurder\n  &7▪ &fBatata Quente\n  &7▪ &fSplegg\n \n  &cTodos os minigames deste lobby estão\n  &cem constante desenvolvimento e poderão\n  &cser removidos a qualquer momento.\n \n&aClique para conectar!\n&7{players} jogando.", 34, ""),
//    SKY_BLOCK("SKULL_ITEM:3 : 1 : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2UxZjVjMDM1MDEwMGQ1NWY5NWQxNzNhZTliODQ4ODJhNTAyNmMwOTVkODhjY2E1ZjliOGU4OTM1NjJhMDZjZiJ9fX0= : 1 : nome>&aSky Block  : desc>\n  &7O modo de jogo mais popular da história\n  &7do Minecraft, agora disponível na Solar Collections.\n \n  &a◦ &fNada de vantagens Pay2Win\n  &a◦ &fSlots ilimitados\n  &a◦ &a+300 &fitens customizados\n  &a◦ &a+45 &fencantamentos especiais\n  &a◦ &a+35 &fminions autônomos\n  &a◦ &a+50 &fcoleções para você evoluir\n  &a◦ &fPerfil Solo ou Coop\n  &a◦ &fBaús do Tesouro\n  &a◦ &fLeilões\n \n&aClique para conectar!\n&7{players} jogando.", 30, "");

    public static Server findBySlot(int slot) {
        return Arrays.stream(Server.values()).filter(server -> server.getSlot() == slot).findFirst().orElse(null);
    }

    public static int getTotalOnlines() {
        return Arrays.stream(Server.values()).mapToInt(Server::getAllOnline).sum();
    }

    private final String item;
    private final int slot;
    private final List<ServerInfo> servers;

    Server(String item, int slot, String... serversIP) {
        this.item = item;
        this.slot = slot;
        this.servers = new ArrayList<>();

        for (String server : serversIP) {
            if (server.split(":").length > 2) {
                String ip = server.split(":")[0];
                String serverName = server.split(":")[2];
                int port = Integer.parseInt(server.split(":")[1]);
                this.servers.add(new ServerInfo(serverName, ip, port));
            }
        }
    }

    public ItemStack getIcon() {
        return BukkitUtils.getItemStackFromString(this.item.replace("{players}", String.valueOf(getAllOnline())));
    }

    public int getAllOnline() {
        return this.listServers().stream().mapToInt(ServerInfo::getOnline).sum();
    }

    public String getItemString() {
        return this.item;
    }

    public int getSlot() {
        return this.slot;
    }

    public void connect(Profile profile) {
        //Balance System
        if (!this.servers.isEmpty()) {
            ServerInfo server = this.servers.stream().sorted(Comparator.comparingInt(ServerInfo::getOnline)).collect(Collectors.toList()).get(0); //Sempre achará o lobbie com menos jogador
            if (server != null) {
                profile.getPlayer().sendMessage("§aConectando...");
                ByteArrayDataOutput output = ByteStreams.newDataOutput();
                output.writeUTF("Connect");
                output.writeUTF(server.getServerName());
                profile.getPlayer().sendPluginMessage(Main.getInstance(), "BungeeCord", output.toByteArray());
            } else {
                profile.getPlayer().sendMessage("§cNão foi possível fazer a conexão!");
            }
        }
    }

    public List<ServerInfo> listServers() {
        return this.servers;
    }
}
