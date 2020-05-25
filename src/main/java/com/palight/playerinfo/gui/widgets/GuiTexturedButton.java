package com.palight.playerinfo.gui.widgets;

import com.palight.playerinfo.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiTexturedButton extends GuiButton {
    protected static final ResourceLocation iconTextures = new ResourceLocation(PlayerInfo.MODID, "textures/gui/icons.png");
    public int width;
    public int height;
    public int xPosition;
    public int yPosition;
    public int xTexture;
    public int yTexture;
    public int id;
    public boolean enabled;
    public boolean visible;
    protected boolean hovered;
    public int packedFGColour;

    public GuiTexturedButton(int id, int x, int y, int xTexture, int yTexture) {
        this(id, x, y, 16, 16, xTexture, yTexture);
    }

    public GuiTexturedButton(int id, int xPosition, int yPosition, int width, int height, int xTexture, int yTexture) {
        super(id, xPosition, yPosition, width, height, "");
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = id;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.xTexture = xTexture;
        this.yTexture = yTexture;
    }

    protected int getHoverState(boolean p_getHoverState_1_) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        } else if (p_getHoverState_1_) {
            i = 2;
        }

        return i;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;
            if (this.packedFGColour != 0) {
                j = this.packedFGColour;
            } else if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }

            mc.getTextureManager().bindTexture(iconTextures);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.xTexture, this.yTexture, this.width, this.height);
        }

    }

    protected void mouseDragged(Minecraft mc, int p_mouseDragged_2_, int p_mouseDragged_3_) {
    }

    public void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_) {
    }

    public boolean mousePressed(Minecraft p_mousePressed_1_, int p_mousePressed_2_, int p_mousePressed_3_) {
        return this.enabled && this.visible && p_mousePressed_2_ >= this.xPosition && p_mousePressed_3_ >= this.yPosition && p_mousePressed_2_ < this.xPosition + this.width && p_mousePressed_3_ < this.yPosition + this.height;
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int p_drawButtonForegroundLayer_1_, int p_drawButtonForegroundLayer_2_) {
    }

    public void playPressSound(SoundHandler p_playPressSound_1_) {
        p_playPressSound_1_.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int p_setWidth_1_) {
        this.width = p_setWidth_1_;
    }
}
