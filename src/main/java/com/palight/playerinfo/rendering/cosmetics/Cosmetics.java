package com.palight.playerinfo.rendering.cosmetics;

import java.util.HashMap;
import java.util.Map;

public class Cosmetics {
    public static Map<String, Cape> CAPES = new HashMap<>();

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
}
