package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.modules.misc.CPSMod;
import net.minecraft.client.Minecraft;

public class CPSWidget extends GuiIngameWidget {
    public CPSWidget(int xPosition, int yPosition) {
        super(xPosition, yPosition, -1, -1);
    }

    @Override
    public void render(Minecraft mc) {
        String cpsText = String.format("CPS: %d | %d", CPSMod.getLeftClicks(), CPSMod.getRightClicks());
        this.height = mc.fontRendererObj.FONT_HEIGHT + 1;
        this.width = mc.fontRendererObj.getStringWidth(cpsText);
        super.render(mc);
        this.drawText(cpsText, xPosition, yPosition + 1);
    }
}
