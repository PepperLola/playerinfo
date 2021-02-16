package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.OldAnimationsGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

public class OldAnimationsMod extends Module {
    public OldAnimationsMod() {
        super("old-animations", "Old Animations", "Use 1.7 animations.", ModuleType.MISC, new OldAnimationsGui(), null);
    }

    public void attemptSwing() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer.getItemInUseCount() > 0) {
            boolean mouseDown = mc.gameSettings.keyBindAttack.isKeyDown() && mc.gameSettings.keyBindUseItem.isKeyDown();
            if (mouseDown && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                swingItem(mc.thePlayer);
            }
        }
    }

    private void swingItem(EntityPlayerSP entityplayersp) {
        int swingAnimationEnd = entityplayersp.isPotionActive(Potion.digSpeed) ? (6 - (1 +
                entityplayersp.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (entityplayersp.isPotionActive(Potion.digSlowdown) ? (6 + (1 +
                entityplayersp.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
        if (!entityplayersp.isSwingInProgress || entityplayersp.swingProgressInt >= swingAnimationEnd / 2 || entityplayersp.swingProgressInt < 0) {
            entityplayersp.swingProgressInt = -1;
            entityplayersp.isSwingInProgress = true;
        }
    }
}
