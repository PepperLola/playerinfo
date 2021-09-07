package com.palight.playerinfo.gui.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiWidgetOptions extends GuiCustomWidget {

    private GuiIngameWidget widget;
    private boolean shouldShow = false;
    private boolean reversed = false;

    public GuiWidgetOptions(GuiIngameWidget widget, int id, int xPosition, int yPosition, int width, int height) {
        super(id, xPosition, yPosition, width, height);
        this.widget = widget;
    }

    @Override
    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        super.drawWidget(mc, mouseX, mouseY);

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(PlayerInfo.MODID, "textures/gui/widget_selection_icons.png"));

        this.xPosition = widget.getPosition().getX();
        this.yPosition = widget.getPosition().getY() + widget.height + 2;

        ScaledResolution res = new ScaledResolution(mc);
        if (this.yPosition + this.height > res.getScaledHeight()) {
            reversed = true;
            this.yPosition = widget.getPosition().getY() - 2 - this.height;
        } else {
            reversed = false;
        }

        this.width = widget.width;
        this.height = 8;

        this.drawGradientRect(
                this.xPosition,
                this.yPosition,
                this.xPosition + this.width,
                this.yPosition + this.height,
                0x99000000,
                0x99000000
        );

        int xOffset = widget.getModule().isEnabled() ? 0 : 32;
        drawScaledCustomSizeModalRect(
                this.xPosition,
                this.yPosition,
                xOffset,
                0,
                32,
                32,
                this.height,
                this.height,
                512,
                512
        );

        xOffset = widget.getPosition().isChroma() ? 96 : 64;
        drawScaledCustomSizeModalRect(
                this.xPosition + this.height,
                this.yPosition,
                xOffset,
                0,
                32,
                32,
                this.height,
                this.height,
                512,
                512
        );

        xOffset = widget.getModule().shouldRenderBackground() ? 128 : 160;
        drawScaledCustomSizeModalRect(
                this.xPosition + this.height * 2,
                this.yPosition,
                xOffset,
                0,
                32,
                32,
                this.height,
                this.height,
                512,
                512
        );

        if (widget.getModule().getOptionsGui() != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(GuiTexturedButton.iconTextures);
            drawScaledCustomSizeModalRect(
                    this.xPosition + this.width - this.height,
                    this.yPosition,
                    0,
                    0,
                    128,
                    128,
                    this.height,
                    this.height,
                    1024,
                    128
            );
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        super.mouseClicked(mouseX, mouseY);

        if (NumberUtil.pointIsBetween(
                mouseX,
                mouseY,
                this.xPosition,
                this.yPosition,
                this.xPosition + this.height,
                this.yPosition + this.height
        )) {
            widget.getModule().setEnabled(!widget.getModule().isEnabled());
        } else if (NumberUtil.pointIsBetween(
                mouseX,
                mouseY,
                this.xPosition + this.height,
                this.yPosition,
                this.xPosition + this.height * 2,
                this.yPosition + this.height
        )) {
            widget.toggleChroma();
        } else if (NumberUtil.pointIsBetween(
                mouseX,
                mouseY,
                this.xPosition + this.height * 2,
                this.yPosition,
                this.xPosition + this.height * 3,
                this.yPosition + this.height
        )) {
            widget.getModule().setRenderBackground(!widget.getModule().shouldRenderBackground());
            ModConfiguration.syncFromGUI();
        } else if (widget.getModule().getOptionsGui() != null && NumberUtil.pointIsBetween(
                mouseX,
                mouseY,
                this.xPosition + this.width - this.height,
                this.yPosition,
                this.xPosition + this.width,
                this.yPosition + this.height
        )) {
            Minecraft.getMinecraft().displayGuiScreen(widget.getModule().getOptionsGui());
        }
    }

    public boolean isShouldShow() {
        return shouldShow;
    }

    public void setShouldShow(boolean shouldShow) {
        this.shouldShow = shouldShow;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
}
