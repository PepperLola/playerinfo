package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.EntityRenderTweaksGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import com.palight.playerinfo.util.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityRenderTweaksMod extends Module {

    @ConfigOption
    public int hitTintColor = ColorUtil.getColorInt(255, 0, 0, 75);

    public static boolean flag1 = false;
    public static boolean flag2 = false;

    public EntityRenderTweaksMod() {
        super("entityRenderTweaks", ModuleType.MISC, new EntityRenderTweaksGui(), null);
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent<EntityLiving> event) {
        if (!(event.entity instanceof EntityLiving)) return;
        EntityLiving entityLiving = (EntityLiving) event.entity;

        if (this.isEnabled()) {
            if (entityLiving.hurtTime > 0 || entityLiving.deathTime > 0) {
                float[] colors = ColorUtil.getColorRGBFloats(this.hitTintColor);
                GlStateManager.color(colors[0], colors[1], colors[2], colors[3]);
            }
        }
    }
}
