package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.util.MCUtil;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomGuiScreen extends GuiScreen {
    protected int xSize = 256;
    protected int ySize = 236;

    protected int headerWidth = 76;
    protected int headerHeight = 25;

    protected int footerHeight = 20;

    protected List<GuiCustomWidget> guiElements = new ArrayList<GuiCustomWidget>();

    private ResourceLocation gui = new ResourceLocation(PlayerInfo.MODID, "textures/gui/infogui.png");
    private ResourceLocation guiAssets = new ResourceLocation(PlayerInfo.MODID, "textures/gui/gui_assets.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int guiX = (this.width - xSize) / 2;
        int guiY = (this.height - ySize) / 2;

        this.mc.getTextureManager().bindTexture(gui);
        drawTexturedModalRect(guiX, guiY, 0, 0, xSize, ySize);

        this.mc.getTextureManager().bindTexture(guiAssets);

        int headerX = guiX;
        int headerY = guiY;

//        if ((mouseX >= guiX && mouseX <= guiX + headerWidth) && (mouseY >= guiY && mouseY <= guiY + headerHeight)) {
//            float scl = 1.1f;
//            headerX = (int) Math.floor(guiX - (headerWidth * scl) / 2);
//            headerY = (int) Math.floor(guiY - (headerHeight * scl) / 2);
//            GlStateManager.scale(scl, scl, 1.0f);
//            drawTexturedModalRect(headerX, headerY, 0, 0, headerWidth, headerHeight);
//            GlStateManager.scale(1 / scl, 1 / scl, 1.0f);
//        } else {
        drawTexturedModalRect(headerX, headerY, 0, 0, headerWidth, headerHeight);
//        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {

        for (GuiCustomWidget widget : guiElements) {
            widget.mouseClicked(mouseX, mouseY);
            if (NumberUtil.isBetween(mouseX, widget.xPosition, widget.xPosition + widget.width) &&
                NumberUtil.isBetween(mouseY, widget.yPosition, widget.yPosition + widget.height)) {
                widgetClicked(widget);
            }
        }

        super.mouseClicked(mouseX, mouseY, btn);
    }

    protected void widgetClicked(GuiCustomWidget widget) {

    }
}
