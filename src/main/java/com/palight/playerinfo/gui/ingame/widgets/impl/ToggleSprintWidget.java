package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.impl.movement.ToggleSprintMod;
import com.palight.playerinfo.rendering.font.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class ToggleSprintWidget extends GuiIngameWidget {

    private static KeyBinding sprintKey;
    private static KeyBinding sneakKey;

    private ToggleSprintMod module;

    public ToggleSprintWidget(int xPosition, int yPosition) {
        super(xPosition, yPosition, -1, -1);
    }

    public void render(Minecraft mc) {
        if (module == null) {
            module = ((ToggleSprintMod) PlayerInfo.getModules().get("toggleSprint"));
        }
        if (!module.isEnabled()) return;

        if (sprintKey == null) {
            sprintKey = Minecraft.getMinecraft().gameSettings.keyBindSprint;
        }

        if (sneakKey == null) {
            sneakKey = Minecraft.getMinecraft().gameSettings.keyBindSneak;
        }

        UnicodeFontRenderer fr = PlayerInfo.instance.fontRendererObj;
        String displayText = "";

        if (getPosition().getX() == -1 || getPosition().getY() == -1) {
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            getPosition().setX(getPosition().getX() == -1 ? 2 : getPosition().getX());
            getPosition().setY(getPosition().getY() == -1 ? (int) (res.getScaledHeight() - PlayerInfo.instance.fontRendererObj.getHeight("[Sprinting (Toggled)]")) : getPosition().getY());
        }

        if (getState() == WidgetEditingState.EDITING) {
            displayText = "[Sprinting (Toggled)]";
        } else if (getState() == WidgetEditingState.INGAME) {

            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return;

            if (player.isSneaking()) {
                displayText = "[Sneaking (%s)]";
                if (sneakKey.isKeyDown() && !ToggleSprintMod.isSneakingToggled()) {
                    displayText = String.format(displayText, "Key Held");
                } else if (module.toggleSneakEnabled && ToggleSprintMod.isSneakingToggled()) {
                    displayText = String.format(displayText, "Toggled");
                }
            } else if (player.isSprinting()) {
                displayText = "[Sprinting (%s)]";
                if (sprintKey.isKeyDown() && !ToggleSprintMod.isSprintingToggled()) {
                    displayText = String.format(displayText, "Key Held");
                } else if (module.isEnabled() && ToggleSprintMod.isSprintingToggled()) {
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


        if (!displayText.equals("")) {
            this.width = (int) (fr.getWidth(displayText)) + 2;
            this.height = (int) PlayerInfo.instance.fontRendererObj.getHeight(displayText) + 2;
            super.render(mc);
            drawTextVerticallyCentered(displayText, getPosition().getX() + 1, getPosition().getY() + this.height / 2 + 1);
        }
    }

    @Override
    public boolean shouldRender(Module module) {
        return ((ToggleSprintMod) PlayerInfo.getModules().get("toggleSprint")).toggleSprintWidgetEnabled;
    }
}
