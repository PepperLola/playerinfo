package com.palight.playerinfo.gui.widgets;

import com.palight.playerinfo.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public abstract class GuiCustomWidget extends Gui {
    private ResourceLocation textures = new ResourceLocation(PlayerInfo.MODID, "textures/gui/widgets.png");

    public int xPosition;
    public int yPosition;
    public int height;
    public int width;
    public int id;
    public boolean enabled = true;
    public String displayString;

    public GuiCustomWidget(int id, int xPosition, int yPosition, int width, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.height = height;
        this.width = width;
        this.id = id;
    }

    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(textures);
    }

    public void mouseClicked(int mouseX, int mouseY) {

    }
}
