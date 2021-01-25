package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

public class PingWidget extends GuiIngameWidget {

    public PingWidget() {
        super(-1, -1, 16, 11);
    }

    @Override
    public void render(Minecraft mc) {
        if (Minecraft.getMinecraft().gameSettings.hideGUI) return;
        super.render(mc);

        NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().getSession().getProfile().getId());

        long ping = networkPlayerInfo == null ? 0 : networkPlayerInfo.getResponseTime();

        String displayString = ping + " ms";

        this.width = (int) (PlayerInfo.instance.fontRendererObj.getWidth(displayString) + 4);
        this.height = (int) PlayerInfo.instance.fontRendererObj.getHeight(displayString) + 2;

        drawText(displayString, getPosition().getX() + 2, getPosition().getY() + 1);
    }
}