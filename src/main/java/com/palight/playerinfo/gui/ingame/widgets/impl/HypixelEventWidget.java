package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.events.HypixelEvent;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class HypixelEventWidget extends GuiIngameWidget {
    private int ticksToExist = 0;
    private String title = "";
    private String subtitle = "";
    private final ScaledResolution res;

    public HypixelEventWidget() {
        super(-1, -1, 64, 32);
        res = new ScaledResolution(Minecraft.getMinecraft());
        this.getPosition().setX((res.getScaledWidth() - this.width) / 2);
        this.getPosition().setY((res.getScaledHeight() - this.height) / 2);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void render(Minecraft mc) {
        if (ticksToExist <= 0) {
            title = "";
            subtitle = "";
        }
        if (this.getState() == WidgetState.EDITING) {
            this.title = "palight";
            this.subtitle = "joined the server.";
        }
        if ((!title.equals("") && !subtitle.equals("")) || this.getState() == WidgetState.EDITING) {
            this.width = Math.max(mc.fontRendererObj.getStringWidth(title) + 8, mc.fontRendererObj.getStringWidth(subtitle) + 8);
//            this.xPosition = (res.getScaledWidth() - this.width) / 2;
            super.render(mc);
            drawText(title, getPosition().getX() + (this.width - mc.fontRendererObj.getStringWidth(title)) / 2, getPosition().getY() + (mc.fontRendererObj.FONT_HEIGHT / 2));
            drawText(subtitle, getPosition().getX() + (this.width - mc.fontRendererObj.getStringWidth(subtitle)) / 2, getPosition().getY() + (this.height + mc.fontRendererObj.FONT_HEIGHT) / 2);
        }
    }

    @SubscribeEvent
    public void onFriendJoin(HypixelEvent.FriendEvent event) {
        if (!ModConfiguration.friendAlertsEnabled) return;
        this.title = event.getUsername();
        this.subtitle = HypixelEvent.FriendEvent.FriendEventType.getStringToUse(event.getType()) + " the server.";
        this.ticksToExist = 4 * 20; // seconds * ticks per second
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (ticksToExist > 0) ticksToExist--;
    }
}
