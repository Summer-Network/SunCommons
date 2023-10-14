package com.vulcanth.commons.hook.collections.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;

public class EntityPacketsAdapter extends PacketAdapter {
    public EntityPacketsAdapter(Plugin plugin) {
        super(plugin, PacketType.Play.Server.SPAWN_ENTITY_LIVING);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        // Verifique se é o pacote que você deseja manipular.
        if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
            // Aqui você pode manipular o pacote antes de ser enviado ao cliente.
            // Por exemplo, você pode verificar se é a entidade personalizada que deseja spawnar.
            // Se for, você pode modificar o pacote ou substituí-lo pelo pacote da sua entidade personalizada.
        }
    }

}
