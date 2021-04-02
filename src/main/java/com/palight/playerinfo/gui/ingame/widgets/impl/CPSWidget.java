package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.misc.CPSMod;
import net.minecraft.client.Minecraft;

public class CPSWidget extends GuiIngameWidget {
    public CPSWidget(int xPosition, int yPosition) {
        super(xPosition, yPosition, -1, -1);
    }

    @Override
    public void render(Minecraft mc) {
        String cpsText = String.format("CPS: %d | %d", CPSMod.getLeftClicks(), CPSMod.getRightClicks());
        this.height = (int) (PlayerInfo.instance.fontRendererObj.getHeight(cpsText));
        this.width = (int) PlayerInfo.instance.fontRendererObj.getWidth(cpsText) + 4;
        super.render(mc);
        this.drawTextVerticallyCentered(cpsText, getPosition().getX() + 2, getPosition().getY() + (this.height / 2) + 1);
    }
}
