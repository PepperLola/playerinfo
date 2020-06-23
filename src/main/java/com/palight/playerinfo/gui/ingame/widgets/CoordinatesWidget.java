package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class CoordinatesWidget extends GuiIngameWidget {
    public CoordinatesWidget(int xPosition, int yPosition) {
        super(xPosition, yPosition, 48, 16);
    }

    @Override
    public void render(Minecraft mc) {
        super.render(mc);

        this.height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * 4 + 1;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        int x = (int) Math.floor(player.posX);
        int y = (int) Math.floor(player.posY);
        int z = (int) Math.floor(player.posZ);

        this.drawString(mc.fontRendererObj, "X: " + x, xPosition + 2, yPosition + 1, 0xffffffff);
        this.drawString(mc.fontRendererObj, "Y: " + y, xPosition + 2, yPosition + mc.fontRendererObj.FONT_HEIGHT + 1, 0xffffffff);
        this.drawString(mc.fontRendererObj, "Z: " + z, xPosition + 2, yPosition + mc.fontRendererObj.FONT_HEIGHT * 2 + 1, 0xffffffff);
        this.drawString(mc.fontRendererObj, player.getHorizontalFacing().getName().toUpperCase(), xPosition + 2, yPosition + mc.fontRendererObj.FONT_HEIGHT * 3 + 1, 0xffffffff);
    }
}
