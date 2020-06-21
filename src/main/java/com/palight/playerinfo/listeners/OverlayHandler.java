package com.palight.playerinfo.listeners;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.NoteBlockUtil;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OverlayHandler extends Gui {

    public static String PUMPKIN_CONFIG_NAME = "pumpkinOverlayDisabled";

    @SubscribeEvent
    public void onRenderScreen(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (ModConfiguration.pumpkinOverlayDisabled && mc.inGameHasFocus) {

            if (mc.thePlayer != null) {

                EntityPlayer player = mc.thePlayer;

                mc.getTextureManager().bindTexture(new ResourceLocation(PlayerInfo.MODID, "textures/gui/overlays.png"));

                ItemStack helmet = player.getCurrentArmor(3);

                if (helmet != null && helmet.getItem().equals(Item.getByNameOrId("pumpkin"))) {
                    drawTexturedModalRect(event.resolution.getScaledWidth() - 10 - 16, 10, 0, 0, 16, 16);
                }
            }
        }
    }
}
