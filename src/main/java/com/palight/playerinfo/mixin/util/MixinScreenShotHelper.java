package com.palight.playerinfo.mixin.util;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.misc.ScreenshotHelperMod;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    private static ScreenshotHelperMod module;

    /**
     * @author palight
     * @reason Custom screenshot helper
     */
    @Inject(method="saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;", at = @At("HEAD"), cancellable = true)
    private static void saveScreenshot(File screenshotFile, String name, int width, int height, Framebuffer framebuffer, CallbackInfoReturnable<IChatComponent> ci) {
        if (module == null) {
            module = (ScreenshotHelperMod) PlayerInfo.getModules().get("screenshotHelper");
        }
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

        if (module.isEnabled()) {
            ScreenshotSaverRunnable runnable = new ScreenshotSaverRunnable(width, height, pixelValues, Minecraft.getMinecraft().getFramebuffer(), new File(Minecraft.getMinecraft().mcDataDir, "screenshots"), upload);
            if (module.asyncScreenshotSaving) {
                new Thread(runnable).start();
            } else {
                runnable.run();
            }

            IChatComponent modChatComponent = new ChatComponentText("[playerinfo] ");
            modChatComponent.getChatStyle().setColor(EnumChatFormatting.RED);
            IChatComponent chatComponent = new ChatComponentText("Capturing...");
            chatComponent.getChatStyle().setColor(EnumChatFormatting.WHITE);

            if (!upload) {
                ci.setReturnValue(modChatComponent.appendSibling(chatComponent));
                return;
            }

            chatComponent = new ChatComponentText("Uploading...");
            ci.setReturnValue(modChatComponent.appendSibling(chatComponent));
        }
    }

    /**
     * @author palight
     * @reason Custom screenshot helper
     */
    @Overwrite
    private static File getTimestampedPNGFileForDirectory(File screenshotFile) {
        String date = dateFormat.format(new Date());
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
