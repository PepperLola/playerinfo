package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.LifxGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import com.palight.playerinfo.util.ColorUtil;
import com.palight.playerinfo.util.HttpUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class LifxMod extends Module {

    @ConfigOption
    public String lifxToken = "";
    @ConfigOption
    public String lifxSelector = "";
    @ConfigOption
    public boolean lifxTeamMode = false;

    public static int TEAM_COLOR;

    public LifxMod() {
        super("lifx", ModuleType.MISC, new LifxGui(), null);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer clientPlayer = Minecraft.getMinecraft().thePlayer;
        if (clientPlayer == null) return;
        if (!enabled) return;

        for (int i = 3; i >= 0; i--) {
            ItemStack helmet = clientPlayer.getCurrentArmor(i);

            if (helmet == null) return;

            if (helmet.getItem() instanceof ItemArmor) {
                ItemArmor helmetArmor = (ItemArmor) helmet.getItem();
                if (helmetArmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                    int color = helmetArmor.getColor(helmet);
                    setTeamColor(color);

                    break;
                }
            }
        }
    }

    public void setTeamColor(int color) {
        if (TEAM_COLOR == color) return;

        TEAM_COLOR = color;

        if (lifxTeamMode) {
            setColor(color);
        }
    }

    public void setColor(int color) {
        if (lifxToken.equals("")) return;

        int[] rgba = ColorUtil.getColorRGB(color);

        int red = rgba[0];
        int green = rgba[1];
        int blue = rgba[2];
        int alpha = rgba[3];

        if (alpha == 0)
            alpha = 255;

        System.out.println(String.format("ACTUAL > R: %s | G: %s | B: %s | A: %s", red, green, blue, alpha));

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", "Bearer " + lifxToken);

        HttpUtil.httpPut(String.format("https://api.lifx.com/v1/lights/%s/state", lifxSelector), headers, String.format("{\"power\": \"on\", \"color\": \"rgb:%d,%d,%d\",\"brightness\":%f}", red, green, blue, alpha / 255.0), response -> System.out.println(response.getStatusLine().getStatusCode()));
    }
}
