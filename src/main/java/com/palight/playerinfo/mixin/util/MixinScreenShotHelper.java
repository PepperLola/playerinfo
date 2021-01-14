package com.palight.playerinfo.mixin.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.util.Date;

@Mixin(ScreenShotHelper.class)
public class MixinScreenShotHelper {

    @Final @Shadow private static Logger logger;
    @Final @Shadow private static DateFormat dateFormat;
    @Shadow private static IntBuffer pixelBuffer;
    @Shadow private static int[] pixelValues;

    /**
     * @author palight
     * @reason Custom screenshot helper
     */
    @Overwrite
    public static IChatComponent saveScreenshot(File screenshotFile, String name, int width, int height, Framebuffer framebuffer) {
        try {
            File file = new File(screenshotFile, "screenshots");
            file.mkdir();
            if (OpenGlHelper.isFramebufferEnabled()) {
                width = framebuffer.framebufferTextureWidth;
                height = framebuffer.framebufferTextureHeight;
            }

            int totalPixels = width * height;
            if (pixelBuffer == null || pixelBuffer.capacity() < totalPixels) {
                pixelBuffer = BufferUtils.createIntBuffer(totalPixels);
                pixelValues = new int[totalPixels];
            }

            GL11.glPixelStorei(3333, 1);
            GL11.glPixelStorei(3317, 1);
            pixelBuffer.clear();
            if (OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.bindTexture(framebuffer.framebufferTexture);
                GL11.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
            } else {
                GL11.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
            }

            pixelBuffer.get(pixelValues);
            TextureUtil.processPixelValues(pixelValues, width, height);
            BufferedImage bufferedImage;
            if (OpenGlHelper.isFramebufferEnabled()) {
                bufferedImage = new BufferedImage(framebuffer.framebufferWidth, framebuffer.framebufferHeight, 1);
                int diff = framebuffer.framebufferTextureHeight - framebuffer.framebufferHeight;

                for(int pixelY = diff; pixelY < framebuffer.framebufferTextureHeight; ++pixelY) {
                    for(int pixelX = 0; pixelX < framebuffer.framebufferWidth; ++pixelX) {
                        bufferedImage.setRGB(pixelX, pixelY - diff, pixelValues[pixelY * framebuffer.framebufferTextureWidth + pixelX]);
                    }
                }
            } else {
                bufferedImage = new BufferedImage(width, height, 1);
                bufferedImage.setRGB(0, 0, width, height, pixelValues, 0, width);
            }

            File file2;
            if (name == null) {
                file2 = getTimestampedPNGFileForDirectory(file);
            } else {
                file2 = new File(file, name);
            }

            ImageIO.write(bufferedImage, "png", file2);
            IChatComponent chatComponent = new ChatComponentText(file2.getName());
            chatComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
            chatComponent.getChatStyle().setUnderlined(true);
            return new ChatComponentTranslation("screenshot.success", new Object[]{chatComponent});
        } catch (Exception var11) {
            logger.warn("Couldn't save screenshot", var11);
            return new ChatComponentTranslation("screenshot.failure", new Object[]{var11.getMessage()});
        }
    }

    /**
     * @author palight
     * @reason Custom screenshot helper
     */
    @Overwrite
    private static File getTimestampedPNGFileForDirectory(File screenshotFile) {
        String date = dateFormat.format(new Date()).toString();
        int i = 1;

        while(true) {
            File newFile = new File(screenshotFile, date + (i == 1 ? "" : "_" + i) + ".png");
            if (!newFile.exists()) {
                return newFile;
            }

            ++i;
        }
    }
}
