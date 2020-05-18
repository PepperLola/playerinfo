package com.palight.playerinfo.util;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.palight.playerinfo.gui.SkinManager;
import com.sun.imageio.plugins.common.ImageUtil;
import jdk.internal.dynalink.linker.ConversionComparator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.io.FileUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

        String encodedImage = new BASE64Encoder().encode(imageBytes);

        bos.close();

        return encodedImage;
    }

    public static String getEncodedSkin(String uuid, String imageUrl) throws IOException {
        String encodedSkin = encodeSkin(imageUrl);
        SkinManager.addSkin(uuid, encodedSkin);
        return encodedSkin;
    }
}
