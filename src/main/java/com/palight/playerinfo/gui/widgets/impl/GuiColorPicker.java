package com.palight.playerinfo.gui.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.util.ColorUtil;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
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

    private final int previewX;
    private int previewY;

    private final int previewX2;
    private int previewY2;

    private final int rX; // r bar x
    private final int gX;
    private final int bX;
    private final int aX;

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

            this.drawTexturedModalRect(xPosition, yPosition, 48, 0, width, height);

            setColors(mouseX, mouseY);

            // Draw red bar
            this.drawGradientRect(rX + 3, barY, rX + 3 + barWidth, barY + barHeight,
                    ColorUtil.getColorInt(255, 0, 0, 255),
                    0
            );

            // draw green bar
            this.drawGradientRect(gX + 3, barY, gX + 3 + barWidth, barY + barHeight,
                    ColorUtil.getColorInt(0, 255, 0, 255),
                    0
            );

            // draw blue bar
            this.drawGradientRect(bX + 3, barY, bX + 3 + barWidth, barY + barHeight,
                    ColorUtil.getColorInt(0, 0, 255, 255),
                    0
            );

            int color = this.getColor();
            int[] rgba = ColorUtil.getColorRGB(color);
            color = ColorUtil.getColorInt(rgba[0], rgba[1], rgba[2], 255);

            // draw alpha bar
            this.drawGradientRect(aX + 3, barY, aX + 3 + barWidth, barY + barHeight,
                    color,
                    0
            );

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
