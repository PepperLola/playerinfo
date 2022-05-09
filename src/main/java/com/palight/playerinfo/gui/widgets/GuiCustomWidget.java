package com.palight.playerinfo.gui.widgets;

import com.palight.playerinfo.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public abstract class GuiCustomWidget extends Gui {
    private ResourceLocation textures = new ResourceLocation(PlayerInfo.MODID, "textures/gui/widgets.png");

    public int xPosition, yPosition;
    public int originalX, originalY;
    public int height;
    public int width;
    public int id;
    public boolean enabled = true;
    public String displayString;

    public GuiCustomWidget(int id, int xPosition, int yPosition, int width, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.originalX = xPosition;
        this.originalY = yPosition;
        this.height = height;
        this.width = width;
        this.id = id;
    }

    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(textures);
    }

    public void mouseClicked(int mouseX, int mouseY) {

    }

    /**
     * Arguments
     * 1: X position
     * 2: Y position
     * 3: X offset of target portion from top left of image
     * 4: Y offset "
     * 5: Width of target portion
     * 6: Height of target portion
     * 7: Scaled width to display
     * 8: Scaled height to display
     * 9: Total width of image (specified in resource location)
     * 10: Total height of image
     */
    public static void drawScaledCustomSizeModalRect(
            int x,
            int y,
            int xOffset,
            int yOffset,
            int actualWidth,
            int actualHeight,
            int scaledWidth,
            int scaledHeight,
            int imageWidth,
            int imageHeight
    ) {
        Gui.drawScaledCustomSizeModalRect(x, y, xOffset, yOffset, actualWidth, actualHeight, scaledWidth, scaledHeight, imageWidth, imageHeight);
    }
}
