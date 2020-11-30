package com.palight.playerinfo.modules.impl.gui;

import com.palight.playerinfo.gui.ingame.widgets.impl.ReachDisplayWidget;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class ReachDisplayMod extends Module {

    public static final int displayTime = 1500;
    public static double reach = -1;
    public static long lastAttackTime;

    public ReachDisplayMod() {
        super("reachDisplay", "Reach Display", "Displays the distance between you and the person you hit.", ModuleType.GUI, null, new ReachDisplayWidget());
    }


    @Override
    public void init() {
        this.setEnabled(ModConfiguration.reachDisplayModEnabled);
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "reachDisplayModEnabled", enabled);
        ModConfiguration.syncFromGUI();
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit.getEntityId() == event.target.getEntityId()) {
            final Vec3 vec3 = mc.getRenderViewEntity().getPositionEyes(1.0F);
            reach = mc.objectMouseOver.hitVec.distanceTo(vec3);
        }
        ReachDisplayMod.lastAttackTime = System.currentTimeMillis();
    }
}