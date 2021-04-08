package com.palight.playerinfo.gui.screens.impl.options.modules.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.modules.impl.gui.ArmorMod;
import com.palight.playerinfo.options.ModConfiguration;

import java.util.Arrays;

public class ArmorGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiCheckBox transparentBackground;
    private GuiCheckBox hideDurability;

    private ArmorMod module;

    public ArmorGui() {
        super("screen.armor");
    }

    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = (ArmorMod) PlayerInfo.getModules().get("armor");
        }

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        transparentBackground = new GuiCheckBox(0, buttonX, buttonY, "Transparent background", module.transparentBackground);
        hideDurability = new GuiCheckBox(1, buttonX, buttonY + 20, "Hide durability", module.hideDurability);

        this.guiElements.addAll(Arrays.asList(
                this.transparentBackground,
                this.hideDurability
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (module == null) {
            module = (ArmorMod) PlayerInfo.getModules().get("armor");
        }

        super.widgetClicked(widget);
        if (widget.id == transparentBackground.id) {
            module.transparentBackground = transparentBackground.checked;
        } else if (widget.id == hideDurability.id) {
            module.hideDurability = hideDurability.checked;
        }
        ModConfiguration.syncFromGUI();
    }
}
