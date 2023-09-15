package com.vulcanth.commons.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerInfo {
    private final String serverName;
    private final String ip;
    private final Integer port;
    private int online;
    private int maxPlayers;
    private String motd;

    /**
     * Construtor da classe ServerInfo.
     *
     * @param serverName Nome do servidor.
     * @param ip         Endereço IP do servidor.
     * @param port       Porta do servidor.
     */
    public ServerInfo(String serverName, String ip, Integer port) {
        this.serverName = serverName;
        this.ip = ip;
        this.port = port;
        this.online = 0;
        this.maxPlayers = 0;
        this.motd = "";
    }

    /**
     * Obtém o nome do servidor.
     *
     * @return O nome do servidor.
     */
    public String getServerName() {
        return this.serverName;
    }

    /**
     * Obtém o número de jogadores online.
     *
     * @return O número de jogadores online.
     */
    public int getOnline() {
        return this.online;
    }

    /**
     * Obtém o número máximo de jogadores permitidos.
     *
     * @return O número máximo de jogadores.
     */
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    /**
     * Obtém a mensagem do dia (MOTD) do servidor.
     *
     * @return A mensagem do dia.
     */
    public String getMotd() {
        return this.motd;
    }

    /**
     * Atualiza as informações do servidor.
     */
    public void update() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(this.ip, this.port), 10);

            try (DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                 DataInputStream in = new DataInputStream(socket.getInputStream())) {
                out.write(0xFE);

                StringBuilder response = new StringBuilder();

                int b;
                while ((b = in.read()) != -1) {
                    if (b > 16 && b != 255 && b != 23 && b != 24) {
                        response.append((char) b);
                    }
                }

                String[] data = response.toString().split("§");
                this.online = Integer.parseInt(data[1]);
                this.maxPlayers = Integer.parseInt(data[2]);
                this.motd = data[0].substring(2);
            }
        } catch (IOException ex) {
            // Trate a exceção adequadamente, por exemplo, registre-a ou lance-a novamente conforme necessário.
            this.online = 0;
            this.maxPlayers = 0;
            this.motd = "";
        }
    }
}
