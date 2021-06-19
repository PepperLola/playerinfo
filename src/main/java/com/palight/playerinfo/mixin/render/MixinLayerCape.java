package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.util.RenderUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.net.URISyntaxException;

@Mixin(LayerCape.class)
public class MixinLayerCape {

    @Shadow @Final private RenderPlayer playerRenderer;

    /**
     * @author palight
     * @reason Bind to different textures for animated capes
     */
    @Overwrite
    public void doRenderLayer(AbstractClientPlayer player, float p_doRenderLayer_2_, float p_doRenderLayer_3_, float p_doRenderLayer_4_, float p_doRenderLayer_5_, float p_doRenderLayer_6_, float p_doRenderLayer_7_, float p_doRenderLayer_8_) throws IOException, URISyntaxException {
        if (player.hasPlayerInfo() && !player.isInvisible() && player.isWearing(EnumPlayerModelParts.CAPE) && player.getLocationCape() != null) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int capeTextureId = RenderUtil.bindCapeTexture(player, playerRenderer);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            double lvt_9_1_ = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * (double)p_doRenderLayer_4_ - (player.prevPosX + (player.posX - player.prevPosX) * (double)p_doRenderLayer_4_);
            double lvt_11_1_ = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * (double)p_doRenderLayer_4_ - (player.prevPosY + (player.posY - player.prevPosY) * (double)p_doRenderLayer_4_);
            double lvt_13_1_ = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * (double)p_doRenderLayer_4_ - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double)p_doRenderLayer_4_);
            float lvt_15_1_ = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * p_doRenderLayer_4_;
            double lvt_16_1_ = MathHelper.sin(lvt_15_1_ * 3.1415927F / 180.0F);
            double lvt_18_1_ = -MathHelper.cos(lvt_15_1_ * 3.1415927F / 180.0F);
            float lvt_20_1_ = (float)lvt_11_1_ * 10.0F;
            lvt_20_1_ = MathHelper.clamp_float(lvt_20_1_, -6.0F, 32.0F);
            float lvt_21_1_ = (float)(lvt_9_1_ * lvt_16_1_ + lvt_13_1_ * lvt_18_1_) * 100.0F;
            float lvt_22_1_ = (float)(lvt_9_1_ * lvt_18_1_ - lvt_13_1_ * lvt_16_1_) * 100.0F;
            if (lvt_21_1_ < 0.0F) {
                lvt_21_1_ = 0.0F;
            }

            float lvt_23_1_ = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * p_doRenderLayer_4_;
            lvt_20_1_ += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * p_doRenderLayer_4_) * 6.0F) * 32.0F * lvt_23_1_;
            if (player.isSneaking()) {
                lvt_20_1_ += 25.0F;
            }

            GlStateManager.rotate(6.0F + lvt_21_1_ / 2.0F + lvt_20_1_, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(lvt_22_1_ / 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-lvt_22_1_ / 2.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            this.playerRenderer.getMainModel().renderCape(0.0625F);
            GlStateManager.popMatrix();

//            if (capeTextureId != -1) {
//                GlStateManager.deleteTexture(capeTextureId);
//            }
        }
    }
}
