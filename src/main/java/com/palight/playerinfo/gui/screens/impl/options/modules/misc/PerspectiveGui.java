package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiButton;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.modules.impl.misc.PerspectiveMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;

public class PerspectiveGui extends CustomGuiScreenScrollable {
    private int buttonX;
    private int buttonY;

    private GuiButton mustHoldPerspectiveKeyButton;
    private GuiCheckBox invertVertical;

    private PerspectiveMod module;

    public PerspectiveGui() {
        super("screen.perspective");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = (PerspectiveMod) PlayerInfo.getModules().get("perspective");
        }

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        String buttonText = module.mustHoldKey ? EnumChatFormatting.GREEN + "Hold" + EnumChatFormatting.RESET + "/" + EnumChatFormatting.RED + "Toggle" : EnumChatFormatting.RED + "Hold" + EnumChatFormatting.RESET + "/" + EnumChatFormatting.GREEN + "Toggle";

        mustHoldPerspectiveKeyButton = new GuiButton(0, buttonX, buttonY, 64, 20, buttonText);
        invertVertical = new GuiCheckBox(1, buttonX, buttonY + 32, "Invert vertical axis", module.invertVertical);

        this.guiElements.addAll(Arrays.asList(
                this.mustHoldPerspectiveKeyButton,
                this.invertVertical
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (module == null) {
            module = (PerspectiveMod) PlayerInfo.getModules().get("perspective");
        }
        if (widget.id == mustHoldPerspectiveKeyButton.id) {
            module.mustHoldKey = mustHoldPerspectiveKeyButton.enabled;
            mustHoldPerspectiveKeyButton.displayString = module.mustHoldKey ? EnumChatFormatting.GREEN + "Hold" + EnumChatFormatting.RESET + "/" + EnumChatFormatting.RED + "Toggle" : EnumChatFormatting.RED + "Hold" + EnumChatFormatting.RESET + "/" + EnumChatFormatting.GREEN + "Toggle";
        } else if (widget.id == invertVertical.id) {
            module.invertVertical = invertVertical.checked;
        }
        ModConfiguration.syncFromGUI();
    }
}
