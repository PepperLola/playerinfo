package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.ingame.widgets.impl.DuelTrackerWidget;
import com.palight.playerinfo.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S19PacketEntityStatus;

import java.util.List;

public class DuelTrackerMod extends Module {
    private Minecraft mc;

    public DuelTrackerMod() {
        super("duelTracker", ModuleType.MISC, null, new DuelTrackerWidget());
    }

    public void onEntityStatusPacket (S19PacketEntityStatus packet) {
        if (packet.getOpCode() == 2) {
            if (mc == null) mc = Minecraft.getMinecraft();
            Entity entity = packet.getEntity(mc.theWorld);
            if (entity != null) {
                if (entity.getUniqueID().equals(mc.thePlayer.getUniqueID())) {
                    List<EntityPlayer> players = mc.theWorld.getEntitiesWithinAABB(EntityPlayer.class, mc.thePlayer.getEntityBoundingBox().expand(5, 5, 5));
                    if (players.size() == 0) return;

                    double minDistance = 25;
                    EntityPlayer closest = null;
                    for (EntityPlayer player : players) {
                        double distance = mc.thePlayer.getDistanceToEntity(player);
                        if (distance < minDistance && !player.getName().equals(mc.thePlayer.getName())) {
                            minDistance = distance;
                            closest = player;
                        }
                    }

                    if (closest == null) return;

                    ((DuelTrackerWidget) this.getWidget()).setLastHit(closest, minDistance);
                }
            }
        }
    }
}
