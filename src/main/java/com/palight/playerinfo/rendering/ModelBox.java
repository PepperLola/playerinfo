package com.palight.playerinfo.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ModelBox extends ModelBiped {

    private ModelRenderer headRenderer;

    public ModelBox(ModelPlayer defaultModel, float scale) {
        textureWidth = 16;
        textureHeight = 16;

        headRenderer = new ModelRenderer(defaultModel, 0, 0);

        headRenderer.setRotationPoint(-4.0F, -8.0F, -4.0F);
        headRenderer.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 5.0F);
    }

    public void render(EntityPlayer player, double x, double y, double z) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);

        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
        wr.begin(3, DefaultVertexFormats.POSITION_TEX);

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("pi:textures/gui/infogui.png"));

        wr.pos(10, 0, 0).tex(0, 0).endVertex();
        wr.pos(0, 10, 0).tex(0, 0).endVertex();
        wr.pos(0, 0, 10).tex(0, 0).endVertex();
        wr.pos(10, 10, 0).tex(0, 0).endVertex();

        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();
    }
}
