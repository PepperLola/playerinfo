package com.palight.playerinfo.rendering.training;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.training.targets.TrainingTarget;
import com.palight.playerinfo.util.RenderUtil;
import com.palight.playerinfo.util.math.CylindricalCoords;
import com.palight.playerinfo.util.math.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class TargetRenderer {
    public static void renderTarget(AbstractClientPlayer player, TrainingTarget target) {
        renderTarget(player, target.getPosition(), target.getSize(), target.isPlayerLooking(player));
    }

    public static void renderTarget(AbstractClientPlayer player, CylindricalCoords position, float size, boolean playerLooking) {
        renderTarget(player, position.toVector3d(), size, playerLooking);
    }

    public static void renderTarget(AbstractClientPlayer player, Vector3d position, float size, boolean playerLooking) {
        renderTarget(player, position.x, position.y, position.z, size, playerLooking);
    }

    public static void renderTarget(AbstractClientPlayer player, double x, double y, double z, float size, boolean playerLooking) {
        List<TexturedQuad> quadList = new ArrayList<>();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + player.getEyeHeight(), z);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();

        String texturePath = playerLooking ? "textures/training/target_green.png" : "textures/training/target_red.png";

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(PlayerInfo.MODID, texturePath));


        int textureWidth = 64;
        int textureHeight = 64;

        quadList.add(new TexturedQuad(
                new PositionTextureVertex[]{
                        new PositionTextureVertex(-size / 2, -size / 2f, -size / 2f, 0.0F, 0.0F),
                        new PositionTextureVertex(-size / 2, +size / 2, -size / 2, 0.0F, 8.0F),
                        new PositionTextureVertex(+size / 2, +size / 2, -size / 2, 8.0F, 8.0F),
                        new PositionTextureVertex(+size / 2, -size / 2, -size / 2, 8.0F, 0.0F)
                },
                0, 0,
                textureWidth, textureHeight,
                textureWidth, textureHeight
        ));

        quadList.add(new TexturedQuad(
                new PositionTextureVertex[]{
                        new PositionTextureVertex(-size / 2, -size / 2, +size / 2, 0.0F, 0.0F),
                        new PositionTextureVertex(-size / 2, +size / 2, +size / 2, 0.0F, 8.0F),
                        new PositionTextureVertex(+size / 2, +size / 2, +size / 2, 8.0F, 8.0F),
                        new PositionTextureVertex(+size / 2, -size / 2, +size / 2, 8.0F, 0.0F),
                },
                0, 0,
                textureWidth, textureHeight,
                textureWidth, textureHeight
        ));

        quadList.add(new TexturedQuad(
                new PositionTextureVertex[]{
                        new PositionTextureVertex(-size / 2, -size / 2, -size / 2, 0.0F, 8.0F),
                        new PositionTextureVertex(-size / 2, -size / 2, +size / 2, 0.0F, 0.0F),
                        new PositionTextureVertex(-size / 2, +size / 2, +size / 2, 8.0F, 0.0F),
                        new PositionTextureVertex(-size / 2, size / 2, -size / 2, 8.0F, 8.0F),
                },
                0, 0,
                textureWidth, textureHeight,
                textureWidth, textureHeight
        ));

        quadList.add(new TexturedQuad(
                new PositionTextureVertex[]{
                        new PositionTextureVertex(+size / 2, -size / 2, -size / 2, 0.0F, 0.0F),
                        new PositionTextureVertex(+size / 2, -size / 2, +size / 2, 8.0F, 0.0F),
                        new PositionTextureVertex(+size / 2, +size / 2, +size / 2, 8.0F, 8.0F),
                        new PositionTextureVertex(+size / 2, +size / 2, -size / 2, 0.0F, 8.0F),
                },
                0, 0,
                textureWidth, textureHeight,
                textureWidth, textureHeight
        ));

        quadList.add(new TexturedQuad(
                new PositionTextureVertex[]{
                        new PositionTextureVertex(-size / 2, -size / 2, -size / 2, 0.0F, 8.0F),
                        new PositionTextureVertex(-size / 2, -size / 2, +size / 2, 0.0F, 0.0F),
                        new PositionTextureVertex(+size / 2, -size / 2, +size / 2, 8.0F, 0.0F),
                        new PositionTextureVertex(+size / 2, -size / 2, -size / 2, 8.0F, 8.0F),
                },
                0, 0,
                textureWidth, textureHeight,
                textureWidth, textureHeight
        ));

        quadList.add(new TexturedQuad(
                new PositionTextureVertex[]{
                        new PositionTextureVertex(-size / 2, +size / 2, -size / 2, 0.0F, 0.0F),
                        new PositionTextureVertex(-size / 2, +size / 2, +size / 2, 0.0F, 8.0F),
                        new PositionTextureVertex(+size / 2, +size / 2, +size / 2, 8.0F, 8.0F),
                        new PositionTextureVertex(+size / 2, +size / 2, -size / 2, 8.0F, 0.0F),
                },
                0, 0,
                textureWidth, textureHeight,
                textureWidth, textureHeight
        ));

        for (TexturedQuad quad : quadList) {
            quad.draw(wr, 1.0F);
        }
        GlStateManager.translate(-x, -y, -z);

        GlStateManager.popMatrix();
    }
}
