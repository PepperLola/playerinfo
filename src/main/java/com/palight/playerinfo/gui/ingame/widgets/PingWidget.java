package com.palight.playerinfo.gui.ingame.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

public class PingWidget extends GuiIngameWidget {
    public PingWidget(int xPosition, int yPosition) {
        super(xPosition, yPosition, 16, 11);
    }

    @Override
    public void render(Minecraft mc) {
        super.render(mc);

        NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().getSession().getProfile().getId());

        long ping = (networkPlayerInfo == null) ? 0 : networkPlayerInfo.getResponseTime();

        String displayString = ping + " ms";

        this.width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(displayString) + 4;
        this.height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;

        drawText(ping + " ms", xPosition + 2, yPosition + 2);
    }
}
