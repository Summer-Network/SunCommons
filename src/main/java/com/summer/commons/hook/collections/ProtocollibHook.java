package com.summer.commons.hook.collections;

import com.comphenix.protocol.ProtocolLibrary;
import com.summer.commons.hook.collections.protocollib.EntityPacketsAdapter;
import com.summer.commons.Main;

public class ProtocollibHook {

    public static void setupProtocolLib() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new EntityPacketsAdapter(Main.getInstance()));
    }

}
