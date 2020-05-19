package com.palight.playerinfo.gui.overlay;

import com.palight.playerinfo.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PumpkinOverlayHandler extends Gui {

    public static String PUMPKIN_CONFIG_NAME = "PUMPKIN_OVERLAY_DISABLED";

    @SubscribeEvent
    public void onRenderScreen(RenderGameOverlayEvent.Post event) {
        if (!((Boolean) PlayerInfo.getConfigValue(PUMPKIN_CONFIG_NAME))) return;
        if (!Minecraft.getMinecraft().inGameHasFocus) return;

        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null || mc.thePlayer == null) return;

        EntityPlayer player = mc.thePlayer;

        mc.getTextureManager().bindTexture(new ResourceLocation("pi:textures/gui/overlays.png"));

        ItemStack helmet = player.getCurrentArmor(3);

        if (helmet == null) return;

        if (!helmet.getItem().equals(Item.getByNameOrId("pumpkin"))) return;

        drawTexturedModalRect(event.resolution.getScaledWidth() - 10 - 16, 10, 0, 0, 16, 16);
    }

    @SubscribeEvent
    public void onRenderScreen(RenderGameOverlayEvent event) {
        if (!((Boolean) PlayerInfo.getConfigValue(PUMPKIN_CONFIG_NAME))) return;
        if (event.type.name().equals("HELMET")) {
            event.setCanceled(true);
        }
    }
}
