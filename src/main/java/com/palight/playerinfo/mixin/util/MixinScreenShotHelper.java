package com.palight.playerinfo.mixin.util;

import com.palight.playerinfo.util.ScreenshotSaverRunnable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
    private static final boolean upload = false;

    /**
     * @author palight
     * @reason Custom screenshot helper
     */
    @Overwrite
    public static IChatComponent saveScreenshot(File screenshotFile, String name, int width, int height, Framebuffer framebuffer) {
        final File file1 = new File(Minecraft.getMinecraft().mcDataDir, "screenshots");
        file1.mkdir();

        if (OpenGlHelper.isFramebufferEnabled()) {
            width = framebuffer.framebufferTextureWidth;
            height = framebuffer.framebufferTextureHeight;
        }

        final int i = width * height;

        if (pixelBuffer == null || pixelBuffer.capacity() < i) {
            pixelBuffer = BufferUtils.createIntBuffer(i);
            pixelValues = new int[i];
        }

        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        pixelBuffer.clear();

        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(framebuffer.framebufferTexture);
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        } else {
            GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        }
        pixelBuffer.get(pixelValues);

        new Thread(new ScreenshotSaverRunnable(width, height, pixelValues, Minecraft.getMinecraft().getFramebuffer(), new File(Minecraft.getMinecraft().mcDataDir, "screenshots"), upload)).start();

        IChatComponent modChatComponent = new ChatComponentText("[playerinfo] ");
        modChatComponent.getChatStyle().setColor(EnumChatFormatting.RED);
        IChatComponent chatComponent = new ChatComponentText("Capturing...");
        chatComponent.getChatStyle().setColor(EnumChatFormatting.WHITE);

        if (!upload) {
            return modChatComponent.appendSibling(chatComponent);
        }

        chatComponent = new ChatComponentText("Uploading...");
        return modChatComponent.appendSibling(chatComponent);
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
