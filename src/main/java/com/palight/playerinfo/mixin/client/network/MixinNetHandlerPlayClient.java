package com.palight.playerinfo.mixin.client.network;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.gui.PingMod;
import com.palight.playerinfo.modules.impl.misc.ComboMod;
import com.palight.playerinfo.modules.impl.misc.DuelTrackerMod;
import com.palight.playerinfo.modules.impl.misc.TimeChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
@SideOnly(Side.CLIENT)
public class MixinNetHandlerPlayClient {
    @Shadow
    private Minecraft gameController;

    @Inject(method = "handleEntityStatus", at = @At("HEAD"))
    public void handleEntityStatus(S19PacketEntityStatus packet, CallbackInfo ci) {
        ((ComboMod) PlayerInfo.getModules().get("combo")).onEntityStatusPacket(packet);
        ((DuelTrackerMod) PlayerInfo.getModules().get("duelTracker")).onEntityStatusPacket(packet);
    }

    @Inject(method = "handleStatistics", at = @At("HEAD"))
    public void handleStatistics(S37PacketStatistics packet, CallbackInfo ci) {
        ((PingMod) PlayerInfo.getModules().get("ping")).onEntityStatisticsPacket(packet);
    }

    /**
     * @author Finbarr-1
     * @reason Ignore time changing when custom time is set.
     */
    @Overwrite
    public void handleTimeUpdate(S03PacketTimeUpdate p) {
        TimeChangerMod timeChangerMod = (TimeChangerMod) PlayerInfo.getModules().get("timeChanger");

        if(!timeChangerMod.isEnabled()){
            handleActualPacket(p);
            return;
        }

        TimeChangerMod.Time time = TimeChangerMod.Time.getTime(((TimeChangerMod) PlayerInfo.getModules().get("timeChanger")).selectedTime);

        if (time == null) {
            handleActualPacket(p);
            return;
        }

        handleActualPacket(new S03PacketTimeUpdate(p.getWorldTime(),-timeChangerMod.getTime(time),true));
    }

    private void handleActualPacket(S03PacketTimeUpdate p) {
        if (gameController != null && gameController.theWorld != null) {
            PacketThreadUtil.checkThreadAndEnqueue(p, Minecraft.getMinecraft().getNetHandler(), gameController);
            gameController.theWorld.setTotalWorldTime(p.getTotalWorldTime());
            gameController.theWorld.setWorldTime(p.getWorldTime());
        }
    }
}
