package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.util.RenderUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
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
import java.util.ArrayList;
import java.util.List;

@Mixin(LayerCape.class)
public class MixinLayerCape {

    @Shadow
    @Final
    private RenderPlayer playerRenderer;

    /**
     * @author palight
     * @reason Bind to different textures for animated capes
     */
    @Overwrite
    public void doRenderLayer(AbstractClientPlayer player, float p_doRenderLayer_2_, float p_doRenderLayer_3_, float p_doRenderLayer_4_, float p_doRenderLayer_5_, float p_doRenderLayer_6_, float p_doRenderLayer_7_, float p_doRenderLayer_8_) throws IOException, URISyntaxException {
        if (player.hasPlayerInfo() && !player.isInvisible() && player.isWearing(EnumPlayerModelParts.CAPE) && player.getLocationCape() != null) {
            if (false) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                RenderUtil.bindCapeTexture(player, playerRenderer);

                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.125F);
                double xDiff = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * (double) p_doRenderLayer_4_ - (player.prevPosX + (player.posX - player.prevPosX) * (double) p_doRenderLayer_4_);
                double yDiff = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * (double) p_doRenderLayer_4_ - (player.prevPosY + (player.posY - player.prevPosY) * (double) p_doRenderLayer_4_);
                double zDiff = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * (double) p_doRenderLayer_4_ - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double) p_doRenderLayer_4_);
                float yawDiff = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * p_doRenderLayer_4_;
                double lvt_16_1_ = MathHelper.sin(yawDiff * 3.1415927F / 180.0F);
                double lvt_18_1_ = -MathHelper.cos(yawDiff * 3.1415927F / 180.0F);
                float lvt_20_1_ = (float) yDiff * 10.0F;
                lvt_20_1_ = MathHelper.clamp_float(lvt_20_1_, -6.0F, 32.0F);
                float lvt_21_1_ = (float) (xDiff * lvt_16_1_ + zDiff * lvt_18_1_) * 100.0F;
                float lvt_22_1_ = (float) (xDiff * lvt_18_1_ - zDiff * lvt_16_1_) * 100.0F;
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
            } else {
                renderSmoothCape(player, p_doRenderLayer_2_, p_doRenderLayer_3_,
                p_doRenderLayer_4_, p_doRenderLayer_5_, p_doRenderLayer_6_, p_doRenderLayer_7_,
                p_doRenderLayer_8_);
            }
        }
    }

    public void renderSmoothCape(AbstractClientPlayer player, float p_doRenderLayer_2_, float p_doRenderLayer_3_, float p_doRenderLayer_4_, float p_doRenderLayer_5_, float p_doRenderLayer_6_, float p_doRenderLayer_7_, float p_doRenderLayer_8_) throws IOException, URISyntaxException {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        RenderUtil.bindCapeTexture(player, playerRenderer);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.125F);
        double xDiff = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * (double) p_doRenderLayer_4_ - (player.prevPosX + (player.posX - player.prevPosX) * (double) p_doRenderLayer_4_);
        double yDiff = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * (double) p_doRenderLayer_4_ - (player.prevPosY + (player.posY - player.prevPosY) * (double) p_doRenderLayer_4_);
        double zDiff = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * (double) p_doRenderLayer_4_ - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double) p_doRenderLayer_4_);
        float yawDiff = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * p_doRenderLayer_4_;
        double lvt_16_1_ = MathHelper.sin(yawDiff * 3.1415927F / 180.0F);
        double lvt_18_1_ = -MathHelper.cos(yawDiff * 3.1415927F / 180.0F);
        float lvt_20_1_ = (float) yDiff * 10.0F;
        lvt_20_1_ = MathHelper.clamp_float(lvt_20_1_, -6.0F, 32.0F);
        float lvt_21_1_ = (float) (xDiff * lvt_16_1_ + zDiff * lvt_18_1_) * 100.0F;
        float lvt_22_1_ = (float) (xDiff * lvt_18_1_ - zDiff * lvt_16_1_) * 100.0F;
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

        //(-5, 0, -1) with dimensions (10, 16, 1)

        final int height = 16;
        final int width = 10;

        final int textureWidth = 64;
        final int textureHeight = 32;

        List<TexturedQuad> quadList = new ArrayList<>();



        final int HEIGHT_SEGMENTS = 8;
        final int SEGMENT_HEIGHT = height / HEIGHT_SEGMENTS;
        final float angleDiff = 5 / 180f * 3.1415926f;
        final float startAngle = 0;//0 / 180f * 3.1415926f;

        float lastXDiff = (float) (SEGMENT_HEIGHT * Math.sin(startAngle));
        for (int i = 0; i < HEIGHT_SEGMENTS; i++) {
            float newXDiff = (float) (SEGMENT_HEIGHT * Math.sin(startAngle + angleDiff * i));
            PositionTextureVertex backTopLeft = new PositionTextureVertex(-5, i * SEGMENT_HEIGHT, -1 - lastXDiff, 0.0F, 0.0F);
            PositionTextureVertex frontTopLeft = new PositionTextureVertex(-5, i * SEGMENT_HEIGHT, 0 - lastXDiff, 8.0F, 0.0F);
            PositionTextureVertex frontBottomLeft = new PositionTextureVertex(-5, (i + 1) * SEGMENT_HEIGHT, 0 - lastXDiff - newXDiff, 8.0F, 8.0F);
            PositionTextureVertex backBottomLeft = new PositionTextureVertex(-5, (i + 1) * SEGMENT_HEIGHT, -1 - lastXDiff - newXDiff, 0.0F, 8.0F);

            quadList.add(
                    new TexturedQuad(new PositionTextureVertex[]{backTopLeft, frontTopLeft, frontBottomLeft, backBottomLeft}, 0, 1, 1, 1 + height, textureWidth, textureHeight)
            );

            PositionTextureVertex frontTopRight = new PositionTextureVertex(5, i * SEGMENT_HEIGHT, 0 - lastXDiff, 0.0F, 0.0F);
            PositionTextureVertex backTopRight = new PositionTextureVertex(5, i * SEGMENT_HEIGHT, -1 - lastXDiff, 8.0F, 0.0F);
            PositionTextureVertex backBottomRight = new PositionTextureVertex(5, (i + 1) * SEGMENT_HEIGHT, -1 - lastXDiff - newXDiff, 8.0F, 8.0F);
            PositionTextureVertex frontBottomRight = new PositionTextureVertex(5, (i + 1) * SEGMENT_HEIGHT, 0 - lastXDiff - newXDiff, 0.0F, 8.0F);

            quadList.add(
                    new TexturedQuad(new PositionTextureVertex[]{frontTopRight, backTopRight, backBottomRight, frontBottomRight}, 1 + width, 1, 1 + width + 1, 1 + height, textureWidth, textureHeight)
            );

            lastXDiff = newXDiff + lastXDiff;
        }

        for (int side : new int[]{-1, 0}) {
            int leftOffset = side == 0 ? width : 0;
            lastXDiff = (float) (SEGMENT_HEIGHT * Math.sin(startAngle));
            for (int i = 0; i < HEIGHT_SEGMENTS; i++) {
                float newXDiff = (float) (SEGMENT_HEIGHT * Math.sin(startAngle + angleDiff * i));
//                System.out.println("i: " + i + " | NEWXDIFF: " + newXDiff);
                PositionTextureVertex topLeft = new PositionTextureVertex(-5, i * SEGMENT_HEIGHT, side - lastXDiff, 0.0F, 0.0F);
                PositionTextureVertex topRight = new PositionTextureVertex(5, i * SEGMENT_HEIGHT, side - lastXDiff, 8.0F, 0.0F);
                PositionTextureVertex bottomLeft = new PositionTextureVertex(-5, (i + 1) * SEGMENT_HEIGHT, side - lastXDiff - newXDiff, 0.0F, 8.0F);
                PositionTextureVertex bottomRight = new PositionTextureVertex(5, (i + 1) * SEGMENT_HEIGHT, side - lastXDiff - newXDiff, 8.0F, 8.0F);
                quadList.add(
                        new TexturedQuad(new PositionTextureVertex[]{topLeft, topRight, bottomRight, bottomLeft}, 1 + leftOffset, 1 + i * SEGMENT_HEIGHT, 1 + width + leftOffset, 1 + (i + 1) * SEGMENT_HEIGHT, textureWidth, textureHeight)
                );
                lastXDiff = newXDiff + lastXDiff;
            }
        }

        WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
        for (TexturedQuad quad : quadList) {
            quad.draw(renderer, 0.0625F);
        }

        GlStateManager.popMatrix();
    }
}