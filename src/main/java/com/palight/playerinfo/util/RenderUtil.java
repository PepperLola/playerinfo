package com.palight.playerinfo.util;

import com.palight.playerinfo.gui.SkinManager;
import com.palight.playerinfo.rendering.cosmetics.CapeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

public class RenderUtil {
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
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

        String encodedImage = Arrays.toString(Base64.getEncoder().encode(imageBytes));

        bos.close();

        return encodedImage;
    }

    public static String getEncodedSkin(String uuid, String imageUrl) throws IOException {
        String encodedSkin = encodeSkin(imageUrl);
        SkinManager.addSkin(uuid, encodedSkin);
        return encodedSkin;
    }

    public static ByteBuffer readImageToBuffer(InputStream inputStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(inputStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        Arrays.stream(aint).map(i -> i << 8 | (i >> 24 & 255)).forEach(bytebuffer::putInt);
        bytebuffer.flip();
        return bytebuffer;
    }

    public static BufferedImage readImage(InputStream inputStream) throws IOException {
        return ImageIO.read(inputStream);
    }

    public static InputStream getResourceAsInputStream(String path) {
        if (!path.startsWith("/assets/playerinfo/")) {
            path = "/assets/playerinfo/" + path;
        }
        return Minecraft.class.getResourceAsStream(path);
    }

    public static String convertResourceLocationToPath(ResourceLocation loc) {
        String path = loc.getResourcePath();
        if (!path.startsWith("/assets/playerinfo/")) {
            path = "/assets/playerinfo/" + path;
        }

        return path;
    }

    public static int loadBind(ResourceLocation loc, BufferedImage image) {
        ThreadDownloadImageData data = new ThreadDownloadImageData(null, loc.getResourcePath(), loc, new ImageBufferDownload());
        data.setBufferedImage(image);

        return data.getGlTextureId();
    }

    public static void bindCapeTexture(AbstractClientPlayer player, RenderPlayer playerRenderer) {
        CapeHandler.PlayerData data = CapeHandler.PLAYER_DATA.get(player.getUniqueID());
        if (data != null && data.getCape() != null) {
            Integer dataId = data.getDataId(data.getFrame());
            if (dataId == null) {
                dataId = RenderUtil.loadBind(player.getLocationCape(), data.getCape().getFrames().get(data.getFrame()));
                data.setDataId(data.getFrame(), dataId);
            }
            GlStateManager.bindTexture(dataId);
        } else {
            playerRenderer.bindTexture(player.getLocationCape());
        }
    }

    public static void renderGlint(RenderPlayer playerRenderer, EntityLivingBase entity, ModelBase modelBase, float p_renderGlint_3_, float p_renderGlint_4_, float p_renderGlint_5_, float p_renderGlint_6_, float p_renderGlint_7_, float p_renderGlint_8_, float p_renderGlint_9_) {
        float f = (float)entity.ticksExisted + p_renderGlint_5_;
        playerRenderer.bindTexture(ENCHANTED_ITEM_GLINT_RES);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        float f1 = 0.5F;
        GlStateManager.color(f1, f1, f1, 1.0F);

        for(int i = 0; i < 2; ++i) {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(768, 1);
            float f2 = 0.76F;
            GlStateManager.color(0.5F * f2, 0.25F * f2, 0.8F * f2, 1.0F);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f3 = 0.33333334F;
            GlStateManager.scale(f3, f3, f3);
            GlStateManager.rotate(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, f * (0.001F + (float)i * 0.003F) * 20.0F, 0.0F);
            GlStateManager.matrixMode(5888);
            modelBase.render(entity, p_renderGlint_3_, p_renderGlint_4_, p_renderGlint_6_, p_renderGlint_7_, p_renderGlint_8_, p_renderGlint_9_);
        }

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GlStateManager.disableBlend();
    }
}
