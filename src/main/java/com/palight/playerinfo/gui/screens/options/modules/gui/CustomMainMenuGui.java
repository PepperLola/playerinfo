package com.palight.playerinfo.gui.screens.options.modules.gui;

import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCheckBox;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.resources.I18n;

import java.util.Arrays;

public class CustomMainMenuGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiCheckBox chromaEnabled;

    public CustomMainMenuGui() {
        super(I18n.format("screen.customMainMenu"));
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        chromaEnabled = new GuiCheckBox(0, buttonX, buttonY, "Chroma enabled", ModConfiguration.getBoolean(ModConfiguration.CATEGORY_MAIN_MENU, "mainMenuChroma"));

        this.guiElements.addAll(Arrays.asList(
                this.chromaEnabled
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        super.widgetClicked(widget);
        if (widget.id == chromaEnabled.id) {
            ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MAIN_MENU, "mainMenuChroma", chromaEnabled.checked);
        }
        ModConfiguration.syncFromGUI();
    }
}
