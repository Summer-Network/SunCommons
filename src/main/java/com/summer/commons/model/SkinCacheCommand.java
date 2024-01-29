package com.summer.commons.model;

import java.util.ArrayList;
import java.util.List;

public class SkinCacheCommand {

    private static final List<String> CACHE = new ArrayList<>();

    public static void addSkinProgress(String name) {
        CACHE.add(name);
    }

    public static void removeSkinProgress(String name) {
        CACHE.remove(name);
    }

    public static boolean isSkinProgress(String name) {
        return CACHE.contains(name);
    }

}
