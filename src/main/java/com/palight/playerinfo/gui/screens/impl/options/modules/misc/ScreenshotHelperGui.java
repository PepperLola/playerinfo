package com.palight.playerinfo.gui.screens.impl.options.modules.misc;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreenScrollable;
import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.gui.widgets.impl.GuiCheckBox;
import com.palight.playerinfo.modules.impl.misc.ScreenshotHelperMod;
import com.palight.playerinfo.options.ModConfiguration;

import java.util.Arrays;

public class ScreenshotHelperGui extends CustomGuiScreenScrollable {

    private int buttonX;
    private int buttonY;

    private GuiCheckBox asyncScreenshotSaving;
    private GuiCheckBox copyToClipboard;

    private ScreenshotHelperMod module;

    public ScreenshotHelperGui() {
        super("screen.screenshotHelper");
    }


    @Override
    public void initGui() {
        super.initGui();

        if (module == null) {
            module = (ScreenshotHelperMod) PlayerInfo.getModules().get("screenshotHelper");
        }

        buttonX = guiX + 32;
        buttonY = guiY + 32;

        asyncScreenshotSaving = new GuiCheckBox(1, buttonX, buttonY, "Lagless screenshot saving", module.asyncScreenshotSaving);
        copyToClipboard = new GuiCheckBox(2, buttonX, buttonY + 20, "Copy screenshot to clipboard", module.copyToClipboard);

        this.guiElements.addAll(Arrays.asList(
                this.asyncScreenshotSaving,
                this.copyToClipboard
        ));
    }

    @Override
    protected void widgetClicked(GuiCustomWidget widget) {
        if (module == null) {
            module = (ScreenshotHelperMod) PlayerInfo.getModules().get("screenshotHelper");
        }

        super.widgetClicked(widget);
        if (widget.id == asyncScreenshotSaving.id) {
            module.asyncScreenshotSaving = asyncScreenshotSaving.checked;
        } else if (widget.id == copyToClipboard.id) {
            module.copyToClipboard = copyToClipboard.checked;
        }
        ModConfiguration.syncFromGUI();
    }
}
