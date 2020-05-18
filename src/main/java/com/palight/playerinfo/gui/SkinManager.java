package com.palight.playerinfo.gui;

import java.util.HashMap;
import java.util.Map;

public class SkinManager {
    public static Map<String, String> encodedPlayerSkins = new HashMap<String, String>();

    public static void addSkin(String uuid, String encodedSkin) {
        encodedPlayerSkins.put(uuid, encodedSkin);
    }

    public static String getEncodedString(String uuid) {
        return encodedPlayerSkins.get(uuid);
    }
}
