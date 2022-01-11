package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.animations.EmoteHandler;
import com.palight.playerinfo.animations.emotes.impl.WaveEmote;
import com.palight.playerinfo.gui.ingame.widgets.impl.PingWidget;
import com.palight.playerinfo.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PingMod extends Module {
    public long currentPing = -1;

    private long lastPing = -1L;

    public PingMod() {
        super("ping", ModuleType.GUI, null, new PingWidget());
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        // 5 seconds
        if (System.nanoTime() - Math.abs(lastPing) > 1000000L * 5000) {
            if (Minecraft.getMinecraft().theWorld.isRemote) {
                sendPacket();
            }
            EmoteHandler.startPlayingEmote(Minecraft.getMinecraft().thePlayer.getUniqueID(), new WaveEmote());
        }
    }

    private void sendPacket() {
        Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
        lastPing = System.nanoTime();
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (event.entity == null || Minecraft.getMinecraft() == null || Minecraft.getMinecraft().thePlayer == null) return;
        if (event.entity.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
            this.lastPing = -1L;
        }
    }

    public void onEntityStatisticsPacket (S37PacketStatistics packet) {
        if (lastPing > 0) {
            long diff = (Math.abs(System.nanoTime() - lastPing) / 1000000);
            this.lastPing *= -1;
            this.currentPing = diff;
        }
    }
}
