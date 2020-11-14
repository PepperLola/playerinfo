package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.movement.ToggleSprintMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class ToggleSprintWidget extends GuiIngameWidget {

    private static KeyBinding sprintKey;

    public ToggleSprintWidget(int xPosition, int yPosition) {
        super(xPosition, yPosition, -1, -1);
        sprintKey = Minecraft.getMinecraft().gameSettings.keyBindSprint;
    }

    public void render(Minecraft mc) {
        if (!ModConfiguration.toggleSprintWidgetEnabled) return;
        FontRenderer fr = mc.fontRendererObj;
        this.height = fr.FONT_HEIGHT + 1;
        String displayText = "";

        if (this.xPosition == -1 || this.yPosition == -1) {
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            this.xPosition = xPosition == -1 ? 2 : xPosition;
            this.yPosition = yPosition == -1 ? res.getScaledHeight() - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT : yPosition;
        }

        if (getState() == WidgetState.EDITING) {
            displayText = "[Sprinting (Toggled)]";
        } else if (getState() == WidgetState.INGAME) {

            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return;

            if (player.isSneaking()) {
                displayText = "[Sneaking]";
            } else if (player.isSprinting()) {
                displayText = "[Sprinting (%s)]";
                if (sprintKey.isKeyDown() && !ToggleSprintMod.isSprintingToggled()) {
                    displayText = String.format(displayText, "Key Held");
                } else if (ModConfiguration.toggleSprintModEnabled && ToggleSprintMod.isSprintingToggled()) {
                    displayText = String.format(displayText, "Toggled");
                } else {
                    displayText = String.format(displayText, "Vanilla");
                }
            } else if (player.isAirBorne) {
                if (player.motionY > 0) {
                    displayText = "[Jumping]";
                } else if (player.motionY < -0.1) {
                    displayText = "[Falling]";
                } else if (player.motionY > -0.1 && player.motionY <= 0 && !player.onGround) {
                    displayText = "[Flying Somehow]";
                }
            }
        }

        this.width = fr.getStringWidth(displayText) + 2;

        if (!displayText.equals("")) {
            super.render(mc);
            drawText(displayText, xPosition + 1, yPosition + 1);
        }
    }

    @Override
    public boolean shouldRender(Module module) {
        return ModConfiguration.toggleSprintWidgetEnabled;
    }
}
