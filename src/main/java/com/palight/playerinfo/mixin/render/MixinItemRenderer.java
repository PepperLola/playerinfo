package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.rendering.items.CustomItemRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    @Shadow private ItemStack itemToRender;
    @Shadow private float equippedProgress;
    @Shadow private float prevEquippedProgress;

    private CustomItemRenderer customItemRenderer = new CustomItemRenderer((ItemRenderer) (Object) this);

    /**
     * @author Cubxity
     * @reason 1.7 animations
     */
    @Overwrite
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        customItemRenderer.transformFirstPersonItem(equipProgress, swingProgress);
    }

    /**
     * @author CoalOres
     * @reason 1.7 animations
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        customItemRenderer.renderItemInFirstPerson(partialTicks, prevEquippedProgress, equippedProgress, itemToRender);
    }

//    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"))
//    private void preRenderFire(float partialTicks, CallbackInfo ci) {
//        GlStateManager.pushMatrix();
//        GlStateManager.translate(0, Settings.FIRE_HEIGHT, 0);
//    }
//
//    @Inject(method = "renderFireInFirstPerson", at = @At("RETURN"))
//    private void postRenderFire(float partialTicks, CallbackInfo ci) {
//        GlStateManager.popMatrix();
//    }
}
