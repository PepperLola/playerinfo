package com.palight.playerinfo.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends GuiCustomWidget {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    public boolean visible;
    protected boolean hovered;
    public int packedFGColour;

    public GuiButton(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_, String p_i1020_4_) {
        this(p_i1020_1_, p_i1020_2_, p_i1020_3_, 200, 20, p_i1020_4_);
    }

    public GuiButton(int id, int xPosition, int yPosition, int width, int height, String displayString) {
        super(id, xPosition, yPosition, width, height);
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = id;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.displayString = displayString;
    }

    protected int getHoverState(boolean hovered) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        } else if (hovered) {
            i = 2;
        }

        return i;
    }

    @Override
    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
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

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }

    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public boolean isMouseOver() {
        return this.hovered;
    }


    public void playPressSound(SoundHandler soundHandler) {
        soundHandler.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
