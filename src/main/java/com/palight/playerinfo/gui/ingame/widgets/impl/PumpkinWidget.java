package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
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

            if ((helmet != null && helmet.getItem().equals(Item.getByNameOrId("pumpkin"))) || getState() == WidgetEditingState.EDITING) {
                int defaultX = res.getScaledWidth() - 10 - 16;
                int defaultY = 10;
                getPosition().setX(getPosition().getX() == -1 ? defaultX : getPosition().getX());
                getPosition().setY(getPosition().getY() == -1 ? defaultY : getPosition().getY());
                GlStateManager.color(255, 255, 255);
                this.drawTexturedModalRect(getPosition().getX(), getPosition().getY(), 0, 0, 16, 16);
            }
        }
    }
}
