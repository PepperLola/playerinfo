package com.palight.playerinfo.gui.widgets.impl;

import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class GuiMenuBar extends GuiCustomWidget {

    private CustomGuiScreen[] screens;
    private int arrowButtonWidth = 32;
    private int buttonWidth = 96;

    private GuiSlantedButton backButton;
    private GuiSlantedButton forwardButton;
    private List<GuiSlantedButton> buttonList = new ArrayList<GuiSlantedButton>();

    public GuiMenuBar(int id, int xPosition, int yPosition, int width, int height, CustomGuiScreen[] screens) {
        super(id, xPosition, yPosition, width, height);

        this.screens = screens;

        backButton = new GuiSlantedButton(Integer.MIN_VALUE, xPosition, yPosition, arrowButtonWidth, 16, "<");

        int xOffset = arrowButtonWidth - 8;

        for (int i = 0; i < screens.length; i++) {
            String buttonString = screens[i].getScreenName();
            buttonList.add(new GuiSlantedButton(i, xPosition + xOffset, yPosition, buttonWidth, 16, buttonString));
            xOffset += buttonWidth - 8;
        }

        forwardButton = new GuiSlantedButton(Integer.MAX_VALUE, xPosition + 180 - arrowButtonWidth, yPosition, arrowButtonWidth, 16, ">");
    }

    @Override
    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        super.drawWidget(mc, mouseX, mouseY);

        backButton.drawWidget(mc, mouseX, mouseY);

        for (GuiSlantedButton button : buttonList) {
            button.enabled = true;
            if (button.xPosition + button.width > xPosition + width) {
                button.enabled = false;
                continue;
            }
            button.drawWidget(mc, mouseX, mouseY);
        }

        forwardButton.drawWidget(mc, mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

        if (!NumberUtil.pointIsBetween(mouseX, mouseY, xPosition, yPosition, xPosition + width, yPosition + height)) return;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World playerWorld = player.getEntityWorld();
        BlockPos playerLocation = player.getPosition();

        for (GuiSlantedButton b : buttonList) {
            if (b.mousePressed(mouseX, mouseY)) {
                Minecraft.getMinecraft().displayGuiScreen(screens[b.id]);
                break;
            }
        }

        if (forwardButton.mousePressed(mouseX, mouseY)) {
            buttonList.add(0, buttonList.get(buttonList.size() - 1));
            buttonList.remove(buttonList.size() - 1);

            int xOffset = arrowButtonWidth - 8;

            for (int i = 0; i < screens.length; i++) {
                buttonList.get(i).xPosition = xPosition + xOffset;
                xOffset += buttonWidth - 8;
            }

        } else if (backButton.mousePressed(mouseX, mouseY)) {
            buttonList.add(buttonList.get(0));
            buttonList.remove(0);

            int xOffset = arrowButtonWidth - 8;

            for (int i = 0; i < screens.length; i++) {
                String buttonString = screens[i].getScreenName();
                buttonList.get(i).xPosition = xPosition + xOffset;
                xOffset += buttonWidth - 8;
            }
        }
    }
}
