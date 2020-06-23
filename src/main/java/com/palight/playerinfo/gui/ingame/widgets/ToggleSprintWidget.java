package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class ToggleSprintWidget extends GuiIngameWidget {

    private static KeyBinding sprintKey;

    public ToggleSprintWidget(int xPosition, int yPosition) {
        super(xPosition, yPosition, -1, -1);
        sprintKey = Minecraft.getMinecraft().gameSettings.keyBindSprint;
    }

    public void render(Minecraft mc) {
        FontRenderer fr = mc.fontRendererObj;
        this.height = fr.FONT_HEIGHT + 1;
        super.render(mc);
        String displayText = "";

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        if (player.isSprinting()) {
            displayText = "[Sprinting(%s)]";
            if (sprintKey.isKeyDown()) {
                displayText = String.format(displayText, "Key Held");
            } else if (ModConfiguration.toggleSprintModEnabled) {
                displayText = String.format(displayText, "Toggled");
            } else {
                displayText = String.format(displayText, "Vanilla");
            }
        }

        this.width = fr.getStringWidth(displayText) + 2;

        drawString(fr, displayText, xPosition + 1, yPosition + 1, 0xffffffff);
    }
}
