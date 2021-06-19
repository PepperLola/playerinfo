package com.palight.playerinfo.rendering.cosmetics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Cape {
    BufferedImage image;
    List<BufferedImage> frames = new ArrayList<>();
    String name;
    int totalFrames = 1;
    int fps = 1;
    boolean animated = false;

    public Cape(String name) {
        this(name, 1, 1, false);
    }

    public Cape(String name, int totalFrames, int fps, boolean animated) {
        this.name = name;
        this.totalFrames = totalFrames;
        this.fps = fps;
        this.animated = animated;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public int getFPS() {
        return fps;
    }

    public boolean isAnimated() {
        return animated;
    }

    public List<BufferedImage> getFrames() {
        return frames;
    }

    public void calculateFrames() {
        BufferedImage wholeImage = getImage();
        if (wholeImage == null) return;
        int CAPE_HEIGHT = wholeImage.getHeight() / getTotalFrames();
        BufferedImage subImage;
        for (int i = 0; i < getTotalFrames(); i++) {
            subImage = wholeImage.getSubimage(
                    0,
                    CAPE_HEIGHT * i,
                    wholeImage.getWidth(),
                    CAPE_HEIGHT
            );

            frames.add(subImage);
        }
    }

    @Override
    public String toString() {
        return String.format("CAPE[name=%s,fps=%d,frames=%d,animated=%b]", this.name, this.getFPS(), this.getTotalFrames(), this.isAnimated());
    }
}
