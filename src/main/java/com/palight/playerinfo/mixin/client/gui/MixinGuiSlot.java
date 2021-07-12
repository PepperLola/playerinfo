package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = GuiSlot.class)
public class MixinGuiSlot {
    @Shadow
    public int left;
    @Shadow
    public int right;
    @Shadow
    public int top;
    @Shadow
    public int bottom;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    protected float amountScrolled;
    @Final
    @Shadow
    protected Minecraft mc;
//    @Inject(method = "drawContainerBackground", at = @At("HEAD"), cancellable = true, remap = false)
//    protected void drawContainerBackground(Tessellator tessellator, CallbackInfo ci) {
//        ci.cancel();
//    }
//
//    @Inject(method = "overlayBackground", at = @At("HEAD"), cancellable = true)
//    protected void overlayBackground(int p_overlayBackground_1_, int p_overlayBackground_2_, int p_overlayBackground_3_, int p_overlayBackground_4_, CallbackInfo ci) {
//        ci.cancel();
//    }

    @Shadow
    protected void overlayBackground(int p_overlayBackground_1_, int p_overlayBackground_2_, int p_overlayBackground_3_, int p_overlayBackground_4_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(this.left, p_overlayBackground_2_, 0.0D).tex(0.0D, (float) p_overlayBackground_2_ / 32.0F).color(64, 64, 64, p_overlayBackground_4_).endVertex();
        worldrenderer.pos(this.left + this.width, p_overlayBackground_2_, 0.0D).tex((float) this.width / 32.0F, (float) p_overlayBackground_2_ / 32.0F).color(64, 64, 64, p_overlayBackground_4_).endVertex();
        worldrenderer.pos(this.left + this.width, p_overlayBackground_1_, 0.0D).tex((float) this.width / 32.0F, (float) p_overlayBackground_1_ / 32.0F).color(64, 64, 64, p_overlayBackground_3_).endVertex();
        worldrenderer.pos(this.left, p_overlayBackground_1_, 0.0D).tex(0.0D, (float) p_overlayBackground_1_ / 32.0F).color(64, 64, 64, p_overlayBackground_3_).endVertex();
        tessellator.draw();
    }

    @Shadow
    protected void drawContainerBackground(Tessellator p_drawContainerBackground_1_) {
        WorldRenderer worldrenderer = p_drawContainerBackground_1_.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(this.left, this.bottom, 0.0D).tex((float) this.left / f, (float) (this.bottom + (int) this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
        worldrenderer.pos(this.right, this.bottom, 0.0D).tex((float) this.right / f, (float) (this.bottom + (int) this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
        worldrenderer.pos(this.right, this.top, 0.0D).tex((float) this.right / f, (float) (this.top + (int) this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
        worldrenderer.pos(this.left, this.top, 0.0D).tex((float) this.left / f, (float) (this.top + (int) this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
        p_drawContainerBackground_1_.draw();
    }
}
