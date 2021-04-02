package com.palight.playerinfo.mixin.client.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiPlayerTabOverlay.class)
public class MixinGuiPlayerTabOverlay extends Gui {
    @Shadow @Final private Minecraft mc;
    @Shadow @Final private GuiIngame guiIngame;
    @Shadow private IChatComponent footer;
    @Shadow private IChatComponent header;
    @Shadow private long lastTimeOpened;
    @Shadow private boolean isBeingRendered;
    /**
     * @reason Add ability to render ping as text instead of bars
     * @author palight
     */
    @Overwrite
    protected void drawPing(int p_drawPing_1_, int p_drawPing_2_, int p_drawPing_3_, NetworkPlayerInfo info) {
        if (((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks")).renderPingAsText) {
            String ping = String.valueOf(info.getResponseTime());
            this.mc.fontRendererObj.drawString(ping, p_drawPing_2_ + p_drawPing_1_ - this.mc.fontRendererObj.getStringWidth(ping), p_drawPing_3_, 0xffffffff);
        } else {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(icons);
            byte offset;
            if (info.getResponseTime() < 0) {
                offset = 5;
            } else if (info.getResponseTime() < 150) {
                offset = 0;
            } else if (info.getResponseTime() < 300) {
                offset = 1;
            } else if (info.getResponseTime() < 600) {
                offset = 2;
            } else if (info.getResponseTime() < 1000) {
                offset = 3;
            } else {
                offset = 4;
            }

            this.zLevel += 100.0F;
            this.drawTexturedModalRect(p_drawPing_2_ + p_drawPing_1_ - 11, p_drawPing_3_, 0, 176 + offset * 8, 10, 8);
            this.zLevel -= 100.0F;
        }
    }
}
