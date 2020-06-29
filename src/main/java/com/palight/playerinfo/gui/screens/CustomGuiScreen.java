package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.util.MCUtil;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
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
    protected int guiX;
    protected int guiY;

    protected int leftOffset = 15;

    private String screenName;

    public CustomGuiScreen(String screenName) {
        this.screenName = screenName;
    }

    protected int headerWidth = 76;
    protected int headerHeight = 25;

    protected int footerHeight = 20;

    protected List<GuiCustomWidget> guiElements = new ArrayList<GuiCustomWidget>();

    private ResourceLocation gui = new ResourceLocation(PlayerInfo.MODID, "textures/gui/gui.png");
    private ResourceLocation guiAssets = new ResourceLocation(PlayerInfo.MODID, "textures/gui/gui_assets.png");

    @Override
    public void initGui() {
        guiX = (this.width - xSize) / 2;
        guiY = (this.height - ySize) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        guiX = (this.width - xSize) / 2;
        guiY = (this.height - ySize) / 2;

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

        this.mc.fontRendererObj.drawString(screenName, guiX + (headerWidth / 2) - (mc.fontRendererObj.getStringWidth(screenName) / 2), guiY + (int) Math.floor((headerHeight / 2)) - 5, 0xffffffff);

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

    public void drawTextMultiLine(String text, int x, int y, int color, int maxWidth, boolean splitOnSpaces) {
        String splitChar = splitOnSpaces ? " " : "";
        String[] splitText = text.split(splitChar);

        List<String> displayStrings = new ArrayList<String>();

        String currentString = "";
        int currentWidth = 0;
        // loops over split text
        for (int i = 0; i < splitText.length; i++) {
            // current string, adds spaces back if split on spaces and this isn't the last word in the list
            String displayString = splitText[i] + (i != splitText.length - 1 && splitOnSpaces ? " " : "");
            // adds displayString's width to the line's total width
            currentWidth += fontRendererObj.getStringWidth(displayString);

            // this means it's the end of the line and that we should start a new one
            if (currentWidth > maxWidth) {
                // resets total width for next line, adds this line text to the lines list and resets current text
                currentWidth = 0;
                displayStrings.add(currentString);
                currentString = "";
            }
            // adds display string to current text
            currentString += displayString;
        }

        // adds the last row
        displayStrings.add(currentString);

        for (int i = 0; i < displayStrings.size(); i++) {
            String displayString = displayStrings.get(i);
            this.drawString(fontRendererObj, displayString, x, y + (i * fontRendererObj.FONT_HEIGHT), color);
        }
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
