package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.misc.EntityRenderTweaksMod;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase {

    private EntityRenderTweaksMod entityRenderTweaksMod;

    @Inject(method = "shouldCombineTextures", at = @At("HEAD"), cancellable = true)
    public void shouldCombineTextures(CallbackInfoReturnable<Boolean> ci) {
        if (entityRenderTweaksMod == null) {
            entityRenderTweaksMod = (EntityRenderTweaksMod) PlayerInfo.getModules().get("entityRenderTweaks");
        }

        ci.setReturnValue(entityRenderTweaksMod.isEnabled() && entityRenderTweaksMod.doArmorTint);
    }
}
