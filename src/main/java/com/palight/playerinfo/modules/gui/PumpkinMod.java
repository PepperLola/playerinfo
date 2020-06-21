package com.palight.playerinfo.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PumpkinMod extends Module {
    public PumpkinMod() {
        super("pumpkinOverlay", "Pumpkin Overlay", "Disable the pumpkin overlay.", ModuleType.GUI, null);
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.pumpkinModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "pumpkinModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    @SubscribeEvent
    public void onRenderScreen(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (ModConfiguration.pumpkinModEnabled && mc.inGameHasFocus) {

            if (mc.thePlayer != null) {

                EntityPlayer player = mc.thePlayer;

                mc.getTextureManager().bindTexture(new ResourceLocation(PlayerInfo.MODID, "textures/gui/overlays.png"));

                ItemStack helmet = player.getCurrentArmor(3);

                if (helmet != null && helmet.getItem().equals(Item.getByNameOrId("pumpkin"))) {
                    Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(event.resolution.getScaledWidth() - 10 - 16, 10, 0, 0, 16, 16);
                }
            }
        }
    }
}
