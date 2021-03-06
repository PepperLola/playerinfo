package com.palight.playerinfo.gui.widgets.impl;

import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;

public class GuiCheckBox extends GuiCustomWidget {

    public boolean checked = false;


    public GuiCheckBox(int id, int x, int y, String displayString, boolean checked) {
        super(id, x, y, 16, 16);
        this.displayString = displayString;
        this.checked = checked;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if (NumberUtil.pointIsBetween(mouseX, mouseY, xPosition, yPosition, xPosition + width, yPosition + height)) {
            this.checked = !this.checked;
        }
    }

    @Override
    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        super.drawWidget(mc, mouseX, mouseY);
        if (!checked) {
            drawTexturedModalRect(xPosition, yPosition, 0, 64, width, height);
        } else {
            drawTexturedModalRect(xPosition, yPosition, 16, 64, width, height);
        }

        mc.fontRendererObj.drawString(displayString , xPosition + width + 4, yPosition + (height - 8) / 2, 0xffffffff);
    }
}
