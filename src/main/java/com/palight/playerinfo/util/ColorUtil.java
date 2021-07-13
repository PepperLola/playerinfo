package com.palight.playerinfo.util;

import java.awt.*;
import java.util.regex.Pattern;

public class ColorUtil {
    public static final char COLOR_CHAR = '\u00A7';

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

    public static float[] getColorRGBFloats(int color) {
        int[] colors = getColorRGB(color);
        float[] toReturn = new float[4];
        for (int i = 0; i < Math.min(colors.length, 4); i++) {
            toReturn[i] = colors[i] / 255f;
        }

        return toReturn;
    }

    public static int getChromaColor() {
        return getChromaColor(0);
    }

    public static int getChromaColor(long offset) {
        return Color.HSBtoRGB(((System.currentTimeMillis() * 2 + offset) % 10000L) / 10000.0F, 0.8F, 0.8F);
    }

    public static String stripColor(String input) {
        return input == null ? null : Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]").matcher(input).replaceAll("");
    }
}
