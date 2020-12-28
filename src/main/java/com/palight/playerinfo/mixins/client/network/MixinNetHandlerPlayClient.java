package com.palight.playerinfo.mixins.client.network;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.misc.TimeChangerMod;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetHandlerPlayClient.class)

public class MixinNetHandlerPlayClient {
    @Shadow
    private Minecraft gameController;


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

        TimeChangerMod.Time time = TimeChangerMod.Time.getTime(ModConfiguration.selectedTime);

        if (time == null) {
            handleActualPacket(p);
            return;
        }

        handleActualPacket(new S03PacketTimeUpdate(p.getWorldTime(),-timeChangerMod.getTime(time),true) );
    }

    private void handleActualPacket(S03PacketTimeUpdate p) {
        if (gameController != null && gameController.theWorld != null) {
            PacketThreadUtil.checkThreadAndEnqueue(p, Minecraft.getMinecraft().getNetHandler(), gameController);
            gameController.theWorld.setTotalWorldTime(p.getTotalWorldTime());
            gameController.theWorld.setWorldTime(p.getWorldTime());
        }
    }
}
