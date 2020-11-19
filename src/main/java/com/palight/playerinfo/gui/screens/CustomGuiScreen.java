package com.palight.playerinfo.gui.screens;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

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

    /**
     * Constructor for CustomGuiScreen class.
     * @param screenName Name to be displayed when in the GUI.
     */
    public CustomGuiScreen(String screenName) {
        this.screenName = screenName;
    }

    protected int headerWidth = 76;
    protected int headerHeight = 25;

    protected int footerHeight = 20;

    protected List<GuiCustomWidget> guiElements = new ArrayList<GuiCustomWidget>();

    private ResourceLocation gui = new ResourceLocation(PlayerInfo.MODID, "textures/gui/gui.png");
    private ResourceLocation guiAssets = new ResourceLocation(PlayerInfo.MODID, "textures/gui/gui_assets.png");

    /**
     * Initializes the GUI. Just sets the width and the height of the GUI screen.
     */
    @Override
    public void initGui() {
        guiX = (this.width - xSize) / 2;
        guiY = (this.height - ySize) / 2;
    }

    /**
     * Draws the background, header, screen name and everything GuiScreen would draw.
     * @param mouseX X position of the mouse. Provided by Minecraft.
     * @param mouseY Y position of the mouse. Provided by Minecraft.
     * @param partialTicks Partial ticks. Provided by Minecraft.
     */
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

    /**
     * Tests if a widget has been clicked, then notify the widget.
     * @param mouseX X position of the mouse. Provided by Minecraft.
     * @param mouseY Y position of the mouse. Provided by Minecraft.
     * @param btn Mouse button that was pressed. Provided by Minecraft.
     * @throws IOException If there's an error getting mouse events.
     */
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

    /**
     * Handles widget being clicked.
     * @param widget Widget that was clicked.
     */
    protected void widgetClicked(GuiCustomWidget widget) {

    }

    /**
     * Draws text that wraps when it reaches the max width. Used for drawing module description.
     * @param text Text to be displayed.
     * @param x X position to display the text at.
     * @param y Y position to display the text at.
     * @param color Color the text will be when displayed.
     * @param maxWidth Width the text will wrap when it reaches.
     * @param splitOnSpaces Determines whether text should wrap in the middle of a word or on the space before that word.
     */
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

    /**
     * Gets the screen name.
     * @return Screen name.
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Sets screen name directly.
     * @param screenName Name to be displayed when in the GUI.
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
