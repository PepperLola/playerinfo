package com.palight.playerinfo.rendering;

import com.palight.playerinfo.PlayerInfo;
import net.minecraft.util.ResourceLocation;

public enum Cape {
    NONE(null),
    FISH(new ResourceLocation(PlayerInfo.MODID, "textures/capes/fish_cape.png")),
    WALTER(new ResourceLocation(PlayerInfo.MODID, "textures/capes/walter_cape.png")),
    GLITCH(new ResourceLocation(PlayerInfo.MODID, "textures/capes/glitch_cape.gif"));

    ResourceLocation location;

    Cape(ResourceLocation location) {
        this.location = location;
    }

    public static Cape getCape(String name) {
        for (Cape cape : Cape.values()) {
            if (cape.toString().equalsIgnoreCase(name)) {
                return cape;
            }
        }

        return null;
    }

    public ResourceLocation getLocation() {
        return location;
    }
}
