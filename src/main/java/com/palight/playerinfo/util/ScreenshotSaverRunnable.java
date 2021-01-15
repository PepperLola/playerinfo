package com.palight.playerinfo.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotSaverRunnable implements Runnable {

    private final int width;
    private final int height;
    private final int[] pixelValues;
    private final Framebuffer framebuffer;
    private final File screenshotDir;
    private final boolean upload;

    public ScreenshotSaverRunnable(final int width, final int height, final int[] pixelValues, final Framebuffer framebuffer, final File screenshotDir, final boolean upload) {
        this.width = width;
        this.height = height;
        this.pixelValues = pixelValues;
        this.framebuffer = framebuffer;
        this.screenshotDir = screenshotDir;
        this.upload = upload;
    }

    private static void processPixelValues(final int[] pixels, final int displayWidth, final int displayHeight) {
        final int[] aint = new int[displayWidth];
        for (int yValues = displayHeight / 2, val = 0; val < yValues; ++val) {
            System.arraycopy(pixels, val * displayWidth, aint, 0, displayWidth);
            System.arraycopy(pixels, (displayHeight - 1 - val) * displayWidth, pixels, val * displayWidth, displayWidth);
            System.arraycopy(aint, 0, pixels, (displayHeight - 1 - val) * displayWidth, displayWidth);
        }
    }

    private static File getTimestampedPNGFileForDirectory(File screenshotFile) {
        String dateFormatting = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        int i = 1;

        while(true) {
            File newFile = new File(screenshotFile, dateFormatting + (i == 1 ? "" : "_" + i) + ".png");
            if (!newFile.exists()) {
                return newFile;
            }

            ++i;
        }
    }

    @Override
    public void run() {
        processPixelValues(pixelValues, width, height);

        BufferedImage image;

        File screenshot = getTimestampedPNGFileForDirectory(screenshotDir);

        try {
            if (OpenGlHelper.isFramebufferEnabled()) {
                image = new BufferedImage(framebuffer.framebufferWidth, framebuffer.framebufferHeight, 1);

                int texHeight;

                for (int heightSize = texHeight = framebuffer.framebufferTextureHeight - framebuffer.framebufferHeight; texHeight < framebuffer.framebufferTextureHeight; ++texHeight) {
                    for (int widthSize = 0; widthSize < framebuffer.framebufferWidth; ++widthSize) {
                        image.setRGB(widthSize, texHeight - heightSize, pixelValues[texHeight * framebuffer.framebufferTextureWidth + widthSize]);
                    }
                }
            } else {
                image = new BufferedImage(width, height, 1);
                image.setRGB(0, 0, width, height, pixelValues, 0, width);
            }

            ImageIO.write(image, "png", screenshot);

            if (!upload) {
                IChatComponent modChatComponent = new ChatComponentText("[playerinfo] ");
                modChatComponent.getChatStyle().setColor(EnumChatFormatting.RED);

                IChatComponent chatComponent = new ChatComponentTranslation("screenshot.success");
                chatComponent.getChatStyle().setColor(EnumChatFormatting.WHITE);

                IChatComponent fileComponent = new ChatComponentText(screenshot.getName());
                fileComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, screenshot.getCanonicalPath()));
                fileComponent.getChatStyle().setUnderlined(true);
                fileComponent.getChatStyle().setColor(EnumChatFormatting.WHITE);

                Minecraft.getMinecraft().thePlayer.addChatMessage(modChatComponent.appendSibling(chatComponent).appendSibling(fileComponent));
            } else {
                // imgur uploading here...
            }
        } catch (IOException e) {
            e.printStackTrace();
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("screenshot.failure", e.getMessage()));
        }
    }
}
