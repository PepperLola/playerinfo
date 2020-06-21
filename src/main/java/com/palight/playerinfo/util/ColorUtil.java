package com.palight.playerinfo.util;

public class ColorUtil {
    public static int getColorInt(int r, int g, int b) {
        return (255 << 24) + (r << 16) + (g << 8) + b;
    }

    public static int getColorInt(int r, int g, int b, int a) {
        return (a << 24) + (r << 16) + (g << 8) + b;
    }

    public static int[] getColorRGB(int color) {
        int alpha = (color >> 24) & 255;
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;

        return new int[]{r, g, b, alpha};
    }
}
