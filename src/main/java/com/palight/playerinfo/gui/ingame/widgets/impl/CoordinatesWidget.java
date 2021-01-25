package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class CoordinatesWidget extends GuiIngameWidget {
    public CoordinatesWidget(int xPosition, int yPosition) {
        super(xPosition, yPosition, 48, 16);
    }

    @Override
    public void render(Minecraft mc) {
        super.render(mc);

        this.height = (int) (PlayerInfo.instance.fontRendererObj.getHeight("X: ") * 4 + 1);

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        int x = (int) Math.floor(player.posX);
        int y = (int) Math.floor(player.posY);
        int z = (int) Math.floor(player.posZ);

        drawText("X: " + x, getPosition().getX() + 2, getPosition().getY() + 1);
        drawText("Y: " + y, getPosition().getX() + 2, (int) (getPosition().getY() + PlayerInfo.instance.fontRendererObj.getHeight("X: ") + 1));
        drawText("Z: " + z, getPosition().getX() + 2, (int) (getPosition().getY() + PlayerInfo.instance.fontRendererObj.getHeight("Y: ") * 2 + 1));
        drawText(player.getHorizontalFacing().getName().toUpperCase(), getPosition().getX() + 2, (int) (getPosition().getY() + PlayerInfo.instance.fontRendererObj.getHeight("Z: ") * 3 + 1));
    }
}
