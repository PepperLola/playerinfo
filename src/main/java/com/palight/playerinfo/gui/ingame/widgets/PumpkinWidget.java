package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PumpkinWidget extends GuiIngameWidget {
    public PumpkinWidget() {
        super(-1, -1, 16, 16);
    }

    @Override
    public void render(Minecraft mc) {
        if (mc.thePlayer != null) {

            ScaledResolution res = new ScaledResolution(mc);

            EntityPlayer player = mc.thePlayer;

            mc.getTextureManager().bindTexture(new ResourceLocation(PlayerInfo.MODID, "textures/gui/overlays.png"));

            ItemStack helmet = player.getCurrentArmor(3);

            if ((helmet != null && helmet.getItem().equals(Item.getByNameOrId("pumpkin"))) || getState() == WidgetState.EDITING) {
                int defaultX = res.getScaledWidth() - 10 - 16;
                int defaultY = 10;
                this.xPosition = xPosition == -1 ? defaultX : xPosition;
                this.yPosition = yPosition == -1 ? defaultY : yPosition;
                GlStateManager.color(255, 255, 255);
                Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(xPosition, yPosition, 0, 0, 16, 16);
            }
        }
    }
}
