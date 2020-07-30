package com.palight.playerinfo.gui.ingame.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;

public class HypixelEventWidget extends GuiIngameWidget {
    private int tickToExist = 0;
    private String title;
    private String subtitle;

    public HypixelEventWidget() {
        super(-1, -1, 64, 32);
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        this.xPosition = (res.getScaledWidth() - this.width) / 2;
        this.yPosition = (res.getScaledHeight() - this.width) / 2;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void render(Minecraft mc) {
        super.render(mc);
        if ((title != null && subtitle != null) || this.getState() == WidgetState.EDITING) {
            drawText(title, this.xPosition + (mc.fontRendererObj.getStringWidth(title) / 2), this.yPosition - (mc.fontRendererObj.FONT_HEIGHT / 2));
        }
    }
}
