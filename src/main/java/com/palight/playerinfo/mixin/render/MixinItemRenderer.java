package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import com.palight.playerinfo.modules.impl.misc.OldAnimationsMod;
import com.palight.playerinfo.rendering.items.CustomItemRenderer;
import net.minecraft.client.entity.AbstractClientPlayer;
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

    private DisplayTweaksMod displayTweaksMod;
    private OldAnimationsMod oldAnimationsMod;

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

    /**
     * @reason Custom drinking animations
     * @author palight
     */
    @Overwrite
    private void performDrinking(AbstractClientPlayer p_performDrinking_1_, float p_performDrinking_2_) {
        customItemRenderer.performDrinking(p_performDrinking_1_, p_performDrinking_2_, itemToRender);
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"))
    private void preRenderFire(float partialTicks, CallbackInfo ci) {
        if (displayTweaksMod == null)
            displayTweaksMod = ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks"));
        if (displayTweaksMod.isEnabled() && displayTweaksMod.lowerFire) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, -0.20, 0);
        }
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("RETURN"))
    private void postRenderFire(float partialTicks, CallbackInfo ci) {
        if (displayTweaksMod.isEnabled() && displayTweaksMod.lowerFire) {
            GlStateManager.popMatrix();
        }
    }
}
