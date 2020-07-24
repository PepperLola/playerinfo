package com.palight.playerinfo.util;

import com.palight.playerinfo.gui.SkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ReportedException;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

public class RenderUtil {
    public static void drawEntityOnScreen(int x, int y, int scale, float yaw, float pitch, EntityLivingBase entity) {
        GlStateManager.enableColorMaterial();

        GlStateManager.pushMatrix();

        GlStateManager.translate((float)x, (float)y, 50.0F);

        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);

        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);

        RenderHelper.enableStandardItemLighting();

        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

        renderManager.setPlayerViewY(180.0F);
        renderManager.setRenderShadow(false);

        doRenderPlayer(entity, x, y, scale, 0, 0);

        renderManager.setRenderShadow(true);

        GlStateManager.popMatrix();

        RenderHelper.disableStandardItemLighting();

        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static boolean doRenderPlayer(Entity entity, double x, double y, double z, float p_doRenderEntity_8_, float p_doRenderEntity_9_) {
        Render render = null;
        TextureManager renderEngine = Minecraft.getMinecraft().getRenderManager().renderEngine;
        String skinType = "default";
        render = new RenderPlayer(Minecraft.getMinecraft().getRenderManager(), skinType == "slim");
        if (render != null && renderEngine != null) {
            try {
                render.doRender(entity, x, y, z, p_doRenderEntity_8_, p_doRenderEntity_9_);
            } catch (Throwable var18) {
                throw new ReportedException(CrashReport.makeCrashReport(var18, "Rendering entity in world"));
            }
        } else if (renderEngine != null) {
            return false;
        }

        return true;
    }

    public static void renderPlayerModel(ModelPlayer modelPlayer, float scale, int x, int y) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, 4);
//        GlStateManager.scale(2.0F, 2.0F, 2.0F);

        modelPlayer.bipedHead.render(scale);
        modelPlayer.bipedBody.render(scale);
        modelPlayer.bipedRightArm.render(scale);
        modelPlayer.bipedLeftArm.render(scale);
        modelPlayer.bipedRightLeg.render(scale);
        modelPlayer.bipedLeftLeg.render(scale);

        GlStateManager.translate(0, 0, 1);

        modelPlayer.bipedHeadwear.render(scale);
        modelPlayer.bipedLeftLegwear.render(scale);
        modelPlayer.bipedRightLegwear.render(scale);
        modelPlayer.bipedLeftArmwear.render(scale);
        modelPlayer.bipedRightArmwear.render(scale);
        modelPlayer.bipedBodyWear.render(scale);

        GlStateManager.popMatrix();
    }

    public static String encodeSkin(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        RenderedImage image = ImageIO.read(url);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ImageIO.write(image, "png", bos);

        byte[] imageBytes = bos.toByteArray();

        String encodedImage = Base64.getEncoder().encode(imageBytes).toString();

        bos.close();

        return encodedImage;
    }

    public static String getEncodedSkin(String uuid, String imageUrl) throws IOException {
        String encodedSkin = encodeSkin(imageUrl);
        SkinManager.addSkin(uuid, encodedSkin);
        return encodedSkin;
    }
}
