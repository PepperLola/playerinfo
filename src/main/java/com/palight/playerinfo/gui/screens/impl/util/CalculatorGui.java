package com.palight.playerinfo.gui.screens.impl.util;

import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.impl.GuiCalculator;

import java.io.IOException;

public class CalculatorGui extends CustomGuiScreen {

    private GuiCalculator calculator;

    public CalculatorGui() {
        super("screen.calc");
    }

    @Override
    public void initGui() {
        super.initGui();
        calculator = new GuiCalculator(0, guiX + 2, guiY + headerHeight + 2);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        calculator.drawWidget(mc, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        calculator.mouseClicked(mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, btn);
    }
}
