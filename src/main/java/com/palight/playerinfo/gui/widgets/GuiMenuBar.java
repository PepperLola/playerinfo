package com.palight.playerinfo.gui.widgets;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiMenuBar extends GuiCustomWidget {

    private String[] buttonText;
    private int arrowButtonWidth = 32;
    private int buttonWidth = 96;

    private GuiSlantedButton backButton;
    private GuiSlantedButton forwardButton;
    private List<GuiSlantedButton> buttonList = new ArrayList<GuiSlantedButton>();

    public GuiMenuBar(int id, int xPosition, int yPosition, int width, int height, String[] buttonText) {
        super(id, xPosition, yPosition, width, height);

        this.buttonText = buttonText;

        backButton = new GuiSlantedButton(Integer.MIN_VALUE, xPosition, yPosition, arrowButtonWidth, 16, "<");

        int xOffset = arrowButtonWidth - 8;

        for (int i = 0; i < buttonText.length; i++) {
            String buttonString = buttonText[i];
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
                if (b.id == 0) {
                    player.openGui(PlayerInfo.instance, GuiHandler.INFO_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
                } else if (b.id == 1) {
                    player.openGui(PlayerInfo.instance, GuiHandler.SERVER_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
                } else if (b.id == 2) {
                    player.openGui(PlayerInfo.instance, GuiHandler.INTEGRATION_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
                }
                break;
            }
        }

        if (forwardButton.mousePressed(mouseX, mouseY)) {
            buttonList.add(0, buttonList.get(buttonList.size() - 1));
            buttonList.remove(buttonList.size() - 1);

            int xOffset = arrowButtonWidth - 8;

            for (int i = 0; i < buttonText.length; i++) {
                buttonList.get(i).xPosition = xPosition + xOffset;
                xOffset += buttonWidth - 8;
            }

            System.out.println("FORWARD BUTTON");
        } else if (backButton.mousePressed(mouseX, mouseY)) {
            buttonList.add(buttonList.get(0));
            buttonList.remove(0);

            int xOffset = arrowButtonWidth - 8;

            for (int i = 0; i < buttonText.length; i++) {
                String buttonString = buttonText[i];
                buttonList.get(i).xPosition = xPosition + xOffset;
                xOffset += buttonWidth - 8;
            }

            System.out.println("BACK BUTTON");
        }

        System.out.println(buttonList.toString());
    }
}
