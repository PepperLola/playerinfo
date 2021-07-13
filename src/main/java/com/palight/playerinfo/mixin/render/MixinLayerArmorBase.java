package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.misc.EntityRenderTweaksMod;
import com.palight.playerinfo.util.ColorUtil;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase {

    @Shadow private float alpha;
    @Shadow private float colorR;
    @Shadow private float colorG;
    @Shadow private float colorB;

    private EntityRenderTweaksMod entityRenderTweaksMod;

    @Inject(method = "renderLayer", at = @At("HEAD"))
    private void renderLayer(EntityLivingBase entity, float p_renderLayer_2_, float p_renderLayer_3_, float p_renderLayer_4_, float p_renderLayer_5_, float p_renderLayer_6_, float p_renderLayer_7_, float p_renderLayer_8_, int p_renderLayer_9_, CallbackInfo ci) {
        if (entityRenderTweaksMod == null) {
            entityRenderTweaksMod = (EntityRenderTweaksMod) PlayerInfo.getModules().get("entityRenderTweaks");
        }

        if (entityRenderTweaksMod.isEnabled()) {
            boolean flag = EntityRenderTweaksMod.flag1;
            boolean flag1 = EntityRenderTweaksMod.flag2;
            if (flag && flag1) {
                float[] tintColors = ColorUtil.getColorRGBFloats(entityRenderTweaksMod.hitTintColor);
                this.colorR = tintColors[0];
                this.colorG = tintColors[1];
                this.colorB = tintColors[2];
                this.alpha = tintColors[3];
            }
        }
    }
}
