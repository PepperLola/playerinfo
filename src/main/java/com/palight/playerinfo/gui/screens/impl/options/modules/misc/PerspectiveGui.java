package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.modules.impl.misc.PerspectiveMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;

public class PerspectiveGui extends CustomGuiScreenScrollable {
    private int buttonX;
    private int buttonY;

    private GuiButton mustHoldPerspectiveKeyButton;

    public PerspectiveGui() {
        super(I18n.format("screen.perspective"));
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        String buttonText = ModConfiguration.mustHoldPerspectiveKey ? EnumChatFormatting.GREEN + "Hold" + EnumChatFormatting.RESET + "/" + EnumChatFormatting.RED + "Toggle" : EnumChatFormatting.RED + "Hold" + EnumChatFormatting.RESET + "/" + EnumChatFormatting.GREEN + "Toggle";

        mustHoldPerspectiveKeyButton = new GuiButton(1, buttonX, buttonY, 64, 20, buttonText);

        this.guiElements.addAll(Arrays.asList(
                this.mustHoldPerspectiveKeyButton
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == mustHoldPerspectiveKeyButton.id) {

            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_PERSPECTIVE, "mustHoldPerspectiveKey", !ModConfiguration.mustHoldPerspectiveKey);
            ModConfiguration.syncFromGUI();

            mustHoldPerspectiveKeyButton.displayString = ModConfiguration.mustHoldPerspectiveKey ? EnumChatFormatting.GREEN + "Hold" + EnumChatFormatting.RESET + "/" + EnumChatFormatting.RED + "Toggle" : EnumChatFormatting.RED + "Hold" + EnumChatFormatting.RESET + "/" + EnumChatFormatting.GREEN + "Toggle";
            ((PerspectiveMod) PlayerInfo.getModules().get("perspective")).setMustHoldKey(ModConfiguration.mustHoldPerspectiveKey);
        }
    }
}
