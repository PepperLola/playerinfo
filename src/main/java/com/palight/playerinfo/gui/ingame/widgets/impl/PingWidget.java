package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.gui.PingMod;
import net.minecraft.client.Minecraft;

public class PingWidget extends GuiIngameWidget {

    public PingWidget() {
        super(-1, -1, 16, 11);
    }

    @Override
    public void render(Minecraft mc) {
        super.render(mc);

        String displayString = ((PingMod) getModule()).currentPing + " ms";

        this.width = (int) (PlayerInfo.instance.fontRendererObj.getWidth(displayString) + 4);
        this.height = (int) PlayerInfo.instance.fontRendererObj.getHeight(displayString);

        drawTextVerticallyCentered(displayString, getPosition().getX() + 2, getPosition().getY() + this.height / 2 + 1);
    }
}