package com.palight.playerinfo.util;

public class ColorUtil {
    public static int getColorInt(int r, int g, int b) {
        int color = (255 << 24) + (r << 16) + (g << 8) + b;
        return color;
    }

    public static int[] getColorRGB(int color) {
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;

        return new int[]{r, g, b};
    }
}
