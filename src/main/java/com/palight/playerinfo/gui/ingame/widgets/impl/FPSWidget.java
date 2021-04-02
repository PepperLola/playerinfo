package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
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

        this.width = (int) (PlayerInfo.instance.fontRendererObj.getWidth(displayString) + 4);
        this.height = (int) (PlayerInfo.instance.fontRendererObj.getHeight(displayString));

        drawTextVerticallyCentered(displayString, getPosition().getX() + 2, getPosition().getY() + this.height / 2 + 1);
    }
}