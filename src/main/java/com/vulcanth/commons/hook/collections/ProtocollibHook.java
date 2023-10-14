package com.vulcanth.commons.hook.collections;

import com.comphenix.protocol.ProtocolLibrary;
import com.vulcanth.commons.Main;
import com.vulcanth.commons.hook.collections.protocollib.EntityPacketsAdapter;

public class ProtocollibHook {

    public static void setupProtocolLib() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new EntityPacketsAdapter(Main.getInstance()));
    }

}
