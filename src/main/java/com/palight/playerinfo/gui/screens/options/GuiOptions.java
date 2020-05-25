package com.palight.playerinfo.gui.screens.options;

import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.GuiOptionsList;
import com.palight.playerinfo.options.ModOption;
import net.minecraft.client.gui.GuiListExtended;

import java.io.IOException;

public class GuiOptions extends CustomGuiScreen {

    private GuiListExtended optionsList;

    @Override
    public void initGui() {
        super.initGui();
        optionsList = new GuiOptionsList(this, mc, new ModOption[]{new ModOption.BooleanOption(true, "Test Name", "Test Description")});
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.optionsList.handleMouseInput();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.optionsList.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
