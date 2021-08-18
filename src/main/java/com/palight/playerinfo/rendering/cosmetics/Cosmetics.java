package com.palight.playerinfo.rendering.cosmetics;

import java.util.HashMap;
import java.util.Map;

public class Cosmetics {
    public static Map<String, Cape> CAPES = new HashMap<>();
    public static Map<String, Skin> SKINS = new HashMap<>();

    public static void addCape(String name, Cape cape) {
        System.out.println("ADDING CAPE " + name);
        CAPES.put(name, cape);
    }

    public static Cape getCape(String name) {
        return CAPES.get(name);
    }

    public static Map<String, Cape> getCapes() {
        return CAPES;
    }

    public static void addSkin(String name, Skin skin) {
        SKINS.put(name, skin);
    }

    public static Skin getSkin(String name) {
        return SKINS.get(name);
    }

    public static Map<String, Skin> getSkins() {
        return SKINS;
    }
}
