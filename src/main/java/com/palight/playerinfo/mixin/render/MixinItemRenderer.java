package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.rendering.items.CustomItemRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    @Shadow private ItemStack itemToRender;
    @Shadow private float equippedProgress;
    @Shadow private float prevEquippedProgress;

    private CustomItemRenderer customItemRenderer = new CustomItemRenderer((ItemRenderer) (Object) this);

    /**
     * @author palight
     * @reason 1.7 animations
     */
    @Overwrite
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        customItemRenderer.transformFirstPersonItem(equipProgress, swingProgress);
    }

    /**
     * @author palight
     * @reason 1.7 animations
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        customItemRenderer.renderItemInFirstPerson(partialTicks, prevEquippedProgress, equippedProgress, itemToRender);
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"))
    private void preRenderFire(float partialTicks, CallbackInfo ci) {
        DisplayTweaksMod displayTweaksMod = ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks"));
        if (displayTweaksMod.isEnabled() && ModConfiguration.lowerFire) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, -0.20, 0);
        }
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("RETURN"))
    private void postRenderFire(float partialTicks, CallbackInfo ci) {
        DisplayTweaksMod displayTweaksMod = ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks"));
        if (displayTweaksMod.isEnabled() && ModConfiguration.lowerFire) {
            GlStateManager.popMatrix();
        }
    }
}
