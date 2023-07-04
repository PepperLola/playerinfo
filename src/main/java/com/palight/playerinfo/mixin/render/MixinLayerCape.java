package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.rendering.cosmetics.CapeHolder;
import com.palight.playerinfo.rendering.cosmetics.VerletSimulation;
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
                double lvt_9_1_ = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * (double) p_doRenderLayer_4_ - (player.prevPosX + (player.posX - player.prevPosX) * (double) p_doRenderLayer_4_);
                double lvt_11_1_ = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * (double) p_doRenderLayer_4_ - (player.prevPosY + (player.posY - player.prevPosY) * (double) p_doRenderLayer_4_);
                double lvt_13_1_ = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * (double) p_doRenderLayer_4_ - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double) p_doRenderLayer_4_);
                float lvt_15_1_ = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * p_doRenderLayer_4_;
                double lvt_16_1_ = MathHelper.sin(lvt_15_1_ * 3.1415927F / 180.0F);
                double lvt_18_1_ = -MathHelper.cos(lvt_15_1_ * 3.1415927F / 180.0F);
                float lvt_20_1_ = (float) lvt_11_1_ * 10.0F;
                lvt_20_1_ = MathHelper.clamp_float(lvt_20_1_, -6.0F, 32.0F);
                float lvt_21_1_ = (float) (lvt_9_1_ * lvt_16_1_ + lvt_13_1_ * lvt_18_1_) * 100.0F;
                float lvt_22_1_ = (float) (lvt_9_1_ * lvt_18_1_ - lvt_13_1_ * lvt_16_1_) * 100.0F;
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
                ((CapeHolder) player).updateSimulation(player, 16);
                renderSmoothCape(player, p_doRenderLayer_4_, new float[]{1.570793f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f});
            }
        }
    }

    public void renderSmoothCape(AbstractClientPlayer player, float delta, float[] angles) throws IOException, URISyntaxException {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        RenderUtil.bindCapeTexture(player, playerRenderer);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.2F);
//        double lvt_9_1_ = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * (double) delta - (player.prevPosX + (player.posX - player.prevPosX) * (double) delta);
//        double lvt_11_1_ = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * (double) delta - (player.prevPosY + (player.posY - player.prevPosY) * (double) delta);
//        double lvt_13_1_ = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * (double) delta - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double) delta);
//        float lvt_15_1_ = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * delta;
//        double lvt_16_1_ = MathHelper.sin(lvt_15_1_ * 3.1415927F / 180.0F);
//        double lvt_18_1_ = -MathHelper.cos(lvt_15_1_ * 3.1415927F / 180.0F);
//        float lvt_20_1_ = (float) lvt_11_1_ * 10.0F;
//        lvt_20_1_ = MathHelper.clamp_float(lvt_20_1_, -6.0F, 32.0F);
//        float lvt_21_1_ = (float) (lvt_9_1_ * lvt_16_1_ + lvt_13_1_ * lvt_18_1_) * 100.0F;
//        float lvt_22_1_ = (float) (lvt_9_1_ * lvt_18_1_ - lvt_13_1_ * lvt_16_1_) * 100.0F;
//        if (lvt_21_1_ < 0.0F) {
//            lvt_21_1_ = 0.0F;
//        }
//
//        float lvt_23_1_ = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * delta;
//        lvt_20_1_ += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * delta) * 6.0F) * 32.0F * lvt_23_1_;
//        if (player.isSneaking()) {
//            lvt_20_1_ += 25.0F;
//        }
//
//        GlStateManager.rotate(6.0F + lvt_21_1_ / 2.0F + lvt_20_1_, 1.0F, 0.0F, 0.0F);
//        GlStateManager.rotate(lvt_22_1_ / 2.0F, 0.0F, 0.0F, 1.0F);
//        GlStateManager.rotate(-lvt_22_1_ / 2.0F, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

        //(-5, 0, -1) with dimensions (10, 16, 1)

        final int height = 16;
        final int width = 10;

        final int textureWidth = 64;
        final int textureHeight = 32;

        List<TexturedQuad> quadList = new ArrayList<>();

        VerletSimulation simulation = ((CapeHolder) player).getSimulation();

//        for (int i = 0; i < simulation.getPoints().size(); i++) {
//            VerletSimulation.Point point = simulation.getPoints().get(i);
//            System.out.println("POINT " + i + " - Z: " + point.position.x + " | Y: " + point.position.y);
//        }

        final int HEIGHT_SEGMENTS = 16;
        final int SEGMENT_HEIGHT = height / HEIGHT_SEGMENTS;
        VerletSimulation.Stick zeroStick = simulation.getSticks().get(0);

        for (int i = 0; i < HEIGHT_SEGMENTS - 1; i++) {
            VerletSimulation.Stick stick = simulation.getSticks().get(i);
            float z1 = (stick.pointA.getLerpX(delta) - zeroStick.pointA.getLerpX(delta)) * 1;
            float z2 = (stick.pointB.getLerpX(delta) - zeroStick.pointA.getLerpX(delta)) * 1;
//            if (z1 > 0) z1 = 0;
//            if (z2 > 0) z2 = 0;

            float y1 = (stick.pointA.getLerpY(delta) - zeroStick.pointA.getLerpY(delta)) * 1;
            float y2 = (stick.pointB.getLerpY(delta) - zeroStick.pointA.getLerpY(delta)) * 1;

            PositionTextureVertex backTopLeft = new PositionTextureVertex(-5, -y1, -1 - z1, 0.0F, 0.0F);
            PositionTextureVertex frontTopLeft = new PositionTextureVertex(-5, -y1, 0 - z1, 8.0F, 0.0F);
            // TODO y for next 2 shouldn't be y, should be angled
            PositionTextureVertex frontBottomLeft = new PositionTextureVertex(-5, -y2, 0 - z2, 8.0F, 8.0F);
            PositionTextureVertex backBottomLeft = new PositionTextureVertex(-5, -y2, -1 - z2, 0.0F, 8.0F);

            quadList.add(
                    new TexturedQuad(new PositionTextureVertex[]{backTopLeft, frontTopLeft, frontBottomLeft, backBottomLeft}, 0, 1 + i, 1, 2 + i, textureWidth, textureHeight)
            );

            PositionTextureVertex frontTopRight = new PositionTextureVertex(5, -y1, 0 - z1, 0.0F, 0.0F);
            PositionTextureVertex backTopRight = new PositionTextureVertex(5, -y1, -1 - z1, 8.0F, 0.0F);
            PositionTextureVertex backBottomRight = new PositionTextureVertex(5, -y2, -1 - z2, 8.0F, 8.0F);
            PositionTextureVertex frontBottomRight = new PositionTextureVertex(5, -y2, 0 - z2, 0.0F, 8.0F);

            quadList.add(
                    new TexturedQuad(new PositionTextureVertex[]{frontTopRight, backTopRight, backBottomRight, frontBottomRight}, 1 + width, 1 + i, 1 + width + 1, 2 + i, textureWidth, textureHeight)
            );
        }

        for (int side : new int[]{-1, 0}) {
            int leftOffset = side == -1 ? width : 0;
            for (int i = 0; i < HEIGHT_SEGMENTS - 1; i++) {
                VerletSimulation.Stick stick = simulation.getSticks().get(i);
                float z1 = (stick.pointA.getLerpX(delta) - zeroStick.pointA.getLerpX(delta)) * 1;
                float z2 = (stick.pointB.getLerpX(delta) - zeroStick.pointA.getLerpX(delta)) * 1;
//                float z1 = stick.pointA.getLerpX(delta);
//                float z2 = stick.pointB.getLerpX(delta);
//                if (z1 > 0) z1 = 0;
//                if (z2 > 0) z2 = 0;

//                float y1 = (zeroStick.pointA.getLerpY(delta) - i - stick.pointA.getLerpY(delta)) * 1;
//                float y2 = (zeroStick.pointB.getLerpY(delta) - i - stick.pointB.getLerpY(delta)) * 1;
                float y1 = stick.pointA.getLerpY(delta) - zeroStick.pointA.getLerpY(delta);
                float y2 = stick.pointB.getLerpY(delta) - zeroStick.pointA.getLerpY(delta);

                PositionTextureVertex topLeft = new PositionTextureVertex(-5, -y1, side - z1, 0.0F, 0.0F);
                PositionTextureVertex topRight = new PositionTextureVertex(5, -y1, side - z1, 8.0F, 0.0F);
                PositionTextureVertex bottomLeft = new PositionTextureVertex(-5, -y2, side - z2, 0.0F, 8.0F);
                PositionTextureVertex bottomRight = new PositionTextureVertex(5, -y2, side - z2, 8.0F, 8.0F);

//                System.out.println("POINT " + i + " - X1: " + z1 + " | X2: " + z2);
//                System.out.println("POINT " + i + " - Y1: " + y1 + " | Y2: " + y2);
                quadList.add(
                        new TexturedQuad(new PositionTextureVertex[]{topLeft, topRight, bottomRight, bottomLeft}, 1 + leftOffset, 1 + i * SEGMENT_HEIGHT, 1 + width + leftOffset, 1 + (i + 1) * SEGMENT_HEIGHT, textureWidth, textureHeight)
                );
            }
        }

        PositionTextureVertex backTopLeft = new PositionTextureVertex(-5, 0, -1, 0.0F, 0.0F);
        PositionTextureVertex backTopRight = new PositionTextureVertex(5, 0, -1, 8.0F, 0.0F);
        PositionTextureVertex frontTopRight = new PositionTextureVertex(5, 0, 0, 8.0F, 8.0F);
        PositionTextureVertex frontTopLeft = new PositionTextureVertex(-5, 0, 0, 0.0F, 8.0F);

        quadList.add(new TexturedQuad(new PositionTextureVertex[]{backTopLeft, backTopRight, frontTopRight, frontTopLeft}, 1, 0, width, 1, textureWidth, textureHeight));

        VerletSimulation.Stick lastStick = simulation.getSticks().get(simulation.getSticks().size() - 1);
        float y2 = lastStick.pointB.getLerpY(delta) - zeroStick.pointA.getLerpY(delta);
        float z2 = lastStick.pointB.getLerpX(delta) - zeroStick.pointA.getLerpX(delta);

        PositionTextureVertex backBottomLeft = new PositionTextureVertex(-5, -y2, -1 - z2, 0.0F, 0.0F);
        PositionTextureVertex backBottomRight = new PositionTextureVertex(5, -y2, -1 - z2, 8.0F, 0.0F);
        PositionTextureVertex frontBottomRight = new PositionTextureVertex(5, -y2, -z2, 8.0F, 8.0F);
        PositionTextureVertex frontBottomLeft = new PositionTextureVertex(-5, -y2, -z2, 0.0F, 8.0F);

        quadList.add(new TexturedQuad(new PositionTextureVertex[]{backBottomLeft, backBottomRight, frontBottomRight, frontBottomLeft}, 1 + width, 0, 1 + 2 * width, 1, textureWidth, textureHeight));

//        GlStateManager.translate(player.renderOffsetX, player.renderOffsetY, player.renderOffsetZ);
        WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
        for (TexturedQuad quad : quadList) {
            quad.draw(renderer, 0.0625F);
        }
//        GlStateManager.translate(-player.renderOffsetX, -player.renderOffsetY, -player.renderOffsetZ);

        GlStateManager.popMatrix();
    }
}