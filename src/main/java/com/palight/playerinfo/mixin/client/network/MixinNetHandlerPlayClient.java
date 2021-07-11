package com.palight.playerinfo.mixin.client.network;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.misc.TimeChangerMod;
import com.palight.playerinfo.modules.impl.util.AutomationMod;
import com.palight.playerinfo.util.MCUtil;
import com.palight.playerinfo.util.automation.QueuedClick;
import com.palight.playerinfo.util.random.RandomUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
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

    private long lastCastTimestamp = -1;

    private AutomationMod automationMod;


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

    @Inject(method = "handleEntityVelocity", at = @At("HEAD"))
    public void handleEntityVelocity(S12PacketEntityVelocity packet, CallbackInfo ci) {

        if (automationMod == null) {
            automationMod = ((AutomationMod) PlayerInfo.getModules().get("automation"));
        }

        final int MOTION_Y_THRESHOLD = -200;

        int id = packet.getEntityID();

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null ||
            player.fishEntity == null ||
            player.fishEntity.getEntityId() != id) return;

        if (
                packet.getMotionX() == 0 &&
                packet.getMotionZ() == 0 &&
                packet.getMotionY() < MOTION_Y_THRESHOLD
        ) {
            int delay1 = RandomUtil.randomRange(3, 4);
            int delay2 = RandomUtil.randomRange(23, 33);

            automationMod.addQueuedAction(new QueuedClick(delay1, QueuedClick.ClickType.RIGHT));
            MCUtil.antiAFK();
//            automationMod.addQueuedAction(new QueuedClick(delay2, QueuedClick.ClickType.RIGHT));
        }
    }

    private void handleActualPacket(S03PacketTimeUpdate p) {
        if (gameController != null && gameController.theWorld != null) {
            PacketThreadUtil.checkThreadAndEnqueue(p, Minecraft.getMinecraft().getNetHandler(), gameController);
            gameController.theWorld.setTotalWorldTime(p.getTotalWorldTime());
            gameController.theWorld.setWorldTime(p.getWorldTime());
        }
    }
}
