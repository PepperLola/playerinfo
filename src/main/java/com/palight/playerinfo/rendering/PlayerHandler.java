package com.palight.playerinfo.rendering;

import com.palight.playerinfo.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PlayerHandler {
    private static final HashMap<UUID, PlayerHandler> instances = new HashMap();
    private boolean hasStaticCape = false;
    private boolean hasAnimatedCape = false;
    private Boolean hasCapeGlint = false;
    private boolean upsideDown = false;
    private Boolean hasInfo = false;
    private final EntityPlayer player;
    private HashMap<Integer, BufferedImage> animatedCape;
    private long lastFrameTime = 0L;
    private int lastFrame = 0;
    private final int capeInterval = 100;

    public PlayerHandler(EntityPlayer player) {
        this.player = player;
        instances.put(this.player.getUniqueID(), this);
    }

    public static PlayerHandler getFromPlayer(EntityPlayer player) {
        PlayerHandler playerHandler = instances.get(player.getUniqueID());
        return playerHandler == null ? new PlayerHandler(player) : playerHandler;
    }

    private BufferedImage readTexture(String textureBase64) {
        try {
            byte[] imgBytes = Base64.decodeBase64(textureBase64);
            ByteArrayInputStream bias = new ByteArrayInputStream(imgBytes);
            return ImageIO.read(bias);
        } catch (IOException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public void applyCape(String cape) {
        BufferedImage capeImage = this.readTexture(cape);
        int imageHeight;
        int currentFrame;
        if (capeImage.getHeight() != capeImage.getWidth() / 2) {
            HashMap<Integer, BufferedImage> animatedCape = new HashMap();
            imageHeight = capeImage.getHeight() / (capeImage.getWidth() / 2);

            for(currentFrame = 0; currentFrame < imageHeight; ++currentFrame) {
                BufferedImage frame = new BufferedImage(capeImage.getWidth(), capeImage.getWidth() / 2, 2);
                Graphics frameGraphics = frame.getGraphics();
                frameGraphics.drawImage(capeImage, 0, 0, capeImage.getWidth(), capeImage.getWidth() / 2, 0, currentFrame * (capeImage.getWidth() / 2), capeImage.getWidth(), (currentFrame + 1) * (capeImage.getWidth() / 2), (ImageObserver)null);
                frameGraphics.dispose();
                animatedCape.put(currentFrame, frame);
            }

            this.setAnimatedCape(animatedCape);
        } else {
            int imageWidth = 64;
            imageHeight = 32;
            currentFrame = capeImage.getWidth();

            for(int srcHeight = capeImage.getHeight(); imageWidth < currentFrame || imageHeight < srcHeight; imageHeight *= 2) {
                imageWidth *= 2;
            }

            BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
            Graphics g = imgNew.getGraphics();
            g.drawImage(capeImage, 0, 0, null);
            g.dispose();
            this.applyTexture(((AbstractClientPlayer) this.player).getLocationCape(), imgNew);
            this.setHasStaticCape(true);
        }

    }

    public void setAnimatedCape(HashMap<Integer, BufferedImage> animatedCape) {
        this.animatedCape = animatedCape;
        this.setHasAnimatedCape(true);
        this.loadFramesToResource();
    }

    private void loadFramesToResource() {
        Iterator var1 = this.getAnimatedCape().entrySet().iterator();

        while(var1.hasNext()) {
            Map.Entry<Integer, BufferedImage> entry = (Map.Entry)var1.next();
            ResourceLocation currentResource = new ResourceLocation(PlayerInfo.MODID, String.format("textures/capes/%s/%d", this.getPlayerUUID(), entry.getKey()));
            this.applyTexture(currentResource, entry.getValue());
        }
    }

    private ResourceLocation getFrame() {
        long time = System.currentTimeMillis();
        if (time > this.lastFrameTime + (long)this.capeInterval) {
            int currentFrameNo = this.lastFrame + 1 > this.getAnimatedCape().size() - 1 ? 0 : this.lastFrame + 1;
            this.lastFrame = currentFrameNo;
            this.lastFrameTime = time;
            return new ResourceLocation(PlayerInfo.MODID, String.format("textures/capes/%s/%d", this.player.getUniqueID(), currentFrameNo));
        } else {
            return new ResourceLocation(PlayerInfo.MODID, String.format("textures/capes/%s/%d", this.player.getUniqueID(), this.lastFrame));
        }
    }

    public ResourceLocation getCapeLocation() {
        if (this.hasStaticCape) {
            return new ResourceLocation(PlayerInfo.MODID, "textures/capes/glitch_cape.gif");
        } else {
            return this.hasAnimatedCape ? this.getFrame() : null;
        }
    }

    private void applyTexture(final ResourceLocation resourceLocation, final BufferedImage bufferedImage) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new DynamicTexture(bufferedImage));
        });
    }

    public String toString() {
        return "PlayerHandler{hasStaticCape=" + this.hasStaticCape + ", hasAnimatedCape=" + this.hasAnimatedCape + ", hasCapeGlint=" + this.hasCapeGlint + ", upsideDown=" + this.upsideDown + ", hasInfo=" + this.hasInfo + ", playerUUID=" + this.player.getUniqueID() + ", animatedCape=" + this.animatedCape + ", lastFrameTime=" + this.lastFrameTime + ", lastFrame=" + this.lastFrame + ", capeInterval=" + this.capeInterval + '}';
    }

    public void setHasStaticCape(boolean hasStaticCape) {
        this.hasStaticCape = hasStaticCape;
    }

    public void setHasAnimatedCape(boolean hasAnimatedCape) {
        this.hasAnimatedCape = hasAnimatedCape;
    }

    public Boolean getHasCapeGlint() {
        return this.hasCapeGlint;
    }

    public void setHasCapeGlint(Boolean hasCapeGlint) {
        this.hasCapeGlint = hasCapeGlint;
    }

    public boolean isUpsideDown() {
        return this.upsideDown;
    }

    public void setUpsideDown(boolean upsideDown) {
        this.upsideDown = upsideDown;
    }

    public Boolean getHasInfo() {
        return this.hasInfo;
    }

    public void setHasInfo(Boolean hasInfo) {
        this.hasInfo = hasInfo;
    }

    public UUID getPlayerUUID() {
        return this.player.getUniqueID();
    }

    public HashMap<Integer, BufferedImage> getAnimatedCape() {
        return this.animatedCape;
    }
}
