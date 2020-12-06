package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import net.minecraft.client.Minecraft;

public class FPSWidget extends GuiIngameWidget {

    public FPSWidget() {
        super(-1, -1, 16, 11);
    }

    @Override
    public void render(Minecraft mc) {
        super.render(mc);

        int fps = Minecraft.getDebugFPS();

        String displayString = fps + " fps";

        this.width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(displayString) + 4;
        this.height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;

        drawText(displayString, getPosition().getX() + 2, getPosition().getY() + 1);
    }
}