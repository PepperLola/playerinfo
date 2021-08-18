package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.PingWidget;
import com.palight.playerinfo.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PingMod extends Module {
    public long currentPing = -1;
    private int counter = 0;
    private int updateTimeout = 100; // in ticks

    public PingMod() {
        super("ping", ModuleType.GUI, null, new PingWidget());
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (counter == updateTimeout) {
            updatePing();
            counter %= updateTimeout;
        }

        counter ++;
    }

    public void updatePing() {
        // TODO separate into multiple lines and add null checks
        NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getNetHandler();
        if (netHandlerPlayClient == null) return;

        Session session = Minecraft.getMinecraft().getSession();
        if (session == null || session.getProfile() == null || session.getProfile().getId() == null) return;

        NetworkPlayerInfo networkPlayerInfo = netHandlerPlayClient.getPlayerInfo(session.getProfile().getId());

        if (networkPlayerInfo != null) {
            currentPing = networkPlayerInfo.getResponseTime();
        }
    }
}
