package com.palight.playerinfo.gui.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.util.ColorUtil;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class GuiColorPicker extends GuiCustomWidget {
    protected static final ResourceLocation assets = new ResourceLocation(PlayerInfo.MODID, "textures/gui/widget_assets.png");
    public boolean visible;
    protected boolean mouseDown = false;
    protected int barHeight = 30;
    protected int barWidth = 2;
    public float rP = 0f; // red percent
    public float gP = 0f;
    public float bP = 0f;
    public float aP = 1f;

    private int previewX;
    private int previewY;

    private int previewX2;
    private int previewY2;

    private int rX; // r bar x
    private int gX;
    private int bX;
    private int aX;

    private int barY;

    public GuiColorPicker(int id, int x, int y) {
        this(id, x, y, 48, 64);
    }

    public GuiColorPicker(int id, int x, int y, int width, int height) {
        super(id, x, y, width, height);
        this.visible = true;
        previewX = xPosition + 11;
        previewX2 = previewX + 26;
        rX = xPosition + 9;
        gX = rX + 7;
        bX = gX + 7;
        aX = bX + 7;
    }

    @Override
    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            previewY = yPosition + 5;
            previewY2 = previewY + 16;
            barY = yPosition + 29;

            super.drawWidget(mc, mouseX, mouseY);

            FontRenderer fr = mc.fontRendererObj;

            this.drawTexturedModalRect(xPosition, yPosition, 48, 0, width, height);

            setColors(mouseX, mouseY);
            drawArrows(mc);
        }
    }

    public void displayColor() {
        int color = getColor();
        this.drawGradientRect(previewX, previewY, previewX2, previewY2, color, color);
    }

    public int getColor() {
        int r = (int) Math.floor(rP * 255);
        int g = (int) Math.floor(gP * 255);
        int b = (int) Math.floor(bP * 255);
        int a = (int) Math.floor(aP * 255);
        return ColorUtil.getColorInt(r, g, b, a);
    }

    public void setColors(int mouseX, int mouseY) {
        int hoveredBar = getHoveredBar(mouseX, mouseY);
        System.out.println(hoveredBar);

        if (this.mouseDown) {
            if (hoveredBar == 0) {
                rP = (barHeight - (mouseY - barY)) / (float) barHeight;
            } else if (hoveredBar == 1) {
                gP = (barHeight - (mouseY - barY)) / (float) barHeight;
            } else if (hoveredBar == 2) {
                bP = (barHeight - (mouseY - barY)) / (float) barHeight;
            } else if (hoveredBar == 3) {
                aP = (barHeight - (mouseY - barY)) / (float) barHeight;
            }
        }

        displayColor();
    }

    public void drawArrows(Minecraft mc) {
        mc.getTextureManager().bindTexture(assets);
        this.drawTexturedModalRect(rX, barY + (barHeight - (barHeight * rP)) - 4, 0, 0, 8, 8);
        this.drawTexturedModalRect(gX, barY + (barHeight - (barHeight * gP)) - 4, 0, 0, 8, 8);
        this.drawTexturedModalRect(bX, barY + (barHeight - (barHeight * bP)) - 4, 0, 0, 8, 8);
        this.drawTexturedModalRect(aX, barY + (barHeight - (barHeight * aP)) - 4, 0, 0, 8, 8);
    }

    public int getHoveredBar(int mouseX, int mouseY) {
        if (!(mouseY > barY && mouseY < barY + barHeight)) return -1;

        if (NumberUtil.isBetween(mouseX, rX - 1, rX + barWidth + 2)) return 0;
        if (NumberUtil.isBetween(mouseX, gX - 1, gX + barWidth + 2)) return 1;
        if (NumberUtil.isBetween(mouseX, bX - 1, bX + barWidth + 2)) return 2;
        if (NumberUtil.isBetween(mouseX, aX - 1, aX + barWidth + 2)) return 3;

        return -1;
    }

    public void mousePressed() {
        this.mouseDown = true;
    }

    public void mouseReleased() {
        this.mouseDown = false;
    }
}
