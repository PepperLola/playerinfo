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
        renderTarget(player, target.getPosition(), target.getSize());
    }

    public static void renderTarget(AbstractClientPlayer player, CylindricalCoords position, float size) {
        renderTarget(player, position.toVector3d(), size);
    }

    public static void renderTarget(AbstractClientPlayer player, Vector3d position, float size) {
        renderTarget(player, position.x, position.y, position.z, size);
    }

    public static void renderTarget(AbstractClientPlayer player, double x, double y, double z, float size) {
        List<TexturedQuad> quadList = new ArrayList<>();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(PlayerInfo.MODID, "textures/training/target_red.png"));


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
