package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.rendering.CapeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(Render.class)
public class MixinRender<T extends Entity> {
    @Shadow
    @Final
    protected RenderManager renderManager;

    /**
     * @reason Render playerinfo icon next to name
     * @author palight
     */
    @Overwrite
    protected void renderLivingLabel(T entity, String str, double x, double y, double z, int maxDistance) {
        double distance = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (!(distance > (double) (maxDistance * maxDistance))) {
            FontRenderer fr = ((IMixinRender) this).callGetFontRendererFromRenderManager();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.0F, (float) y + entity.height + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            int offset = 0;
            if (str.equals("deadmau5")) {
                offset = -10;
            }

            int xOffset = 0;
            UUID uuid = entity.getUniqueID();
            if (uuid.equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
                xOffset = 8;
            } else if (CapeHandler.PLAYER_DATA.containsKey(uuid)) {
                if (CapeHandler.PLAYER_DATA.get(uuid).isOnline()) {
                    xOffset = 8;
                }
            }

            int halfStringWidth = fr.getStringWidth(str) / 2;

            if (xOffset > 0) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(PlayerInfo.MODID, "icons/playerinfo_icon.png"));
                Gui.drawScaledCustomSizeModalRect(
                        -halfStringWidth - 1,
                        offset,
                        0,
                        0,
                        1024,
                        1024,
                        8,
                        8,
                        1024,
                        1024
                );
            }

            GlStateManager.disableTexture2D();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldRenderer.pos(-halfStringWidth - 1, -1 + offset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos(-halfStringWidth - 1, 9 + offset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos(halfStringWidth + 1 + xOffset, 9 + offset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos(halfStringWidth + 1 + xOffset, -1 + offset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            fr.drawString(str, -fr.getStringWidth(str) / 2 + xOffset, offset, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fr.drawString(str, -fr.getStringWidth(str) / 2 + xOffset, offset, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }
}
