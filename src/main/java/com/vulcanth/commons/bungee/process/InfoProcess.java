package com.vulcanth.commons.bungee.process;

import com.google.gson.JsonParser;

public class InfoProcess {

    public static String processSkinInfo(String json) {
        return String.valueOf(new JsonParser().parse(json).getAsJsonObject().get("skin_selected"));
    }
}
