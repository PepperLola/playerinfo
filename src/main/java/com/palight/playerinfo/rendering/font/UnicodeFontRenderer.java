package com.palight.playerinfo.rendering.font;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import com.palight.playerinfo.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class UnicodeFontRenderer {
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    public final int FONT_HEIGHT = 9;
    private final int[] colorCodes = {
            0x000000,
            0x0000AA,
            0x00AA00,
            0x00AAAA,
            0xAA0000,
            0xAA00AA,
            0xFFAA00,
            0xAAAAAA,
            0x555555,
            0x5555FF,
            0x55FF55,
            0x55FFFF,
            0xFF5555,
            0xFF55FF,
            0xFFFF55,
            0xFFFFFF
    };
    private final Map<String, Float> cachedStringWidth = new HashMap<>();
    private float antiAliasingFactor;
    private UnicodeFont unicodeFont;
    private int prevScaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    private String name;
    private float size;
    private ScaledResolution resolution;

    private DisplayTweaksMod module;

    public UnicodeFontRenderer(String fontName, float fontSize) {
        name = fontName;
        size = fontSize;
    }

    public UnicodeFontRenderer(Font font) {
        this(font.getFontName(), font.getSize());
    }

    public UnicodeFontRenderer(String fontName, int fontType, int size) {
        this(new Font(fontName, fontType, size));
    }

    private Font getFontByName(String name) throws IOException, FontFormatException {
        String unformatted = name.toLowerCase().replaceAll(" ", "");
        if (unformatted.equals("segoeui")) {
            return getFontFromInput("/assets/playerinfo/fonts/Segoe-UI.ttf");
        } else if (unformatted.equals("segoeuisb")) {
            return getFontFromInput("/assets/playerinfo/fonts/Segoe-UI-Semi-Bold.ttf");
        } else if (unformatted.equals("roboto")) {
            return getFontFromInput("/assets/playerinfo/fonts/RobotoMono-Regular.ttf");
        } else if (unformatted.equals("robotosb")) {
            return getFontFromInput("/assets/playerinfo/fonts/RobotoMono-SemiBold.ttf");
        }
        return getFontFromInput("/assets/playerinfo/fonts/" + name + ".ttf");
    }

    private Font getFontFromInput(String path) throws IOException, FontFormatException {
        return Font.createFont(Font.TRUETYPE_FONT, UnicodeFontRenderer.class.getResourceAsStream(path));
    }

    public enum Baseline {
        TOP, MIDDLE, BOTTOM;
    }

    public enum Alignment {
        LEFT, CENTER, RIGHT;
    }

    private float transformXWithAlignment(float x, float width, Alignment alignment) {
        switch (alignment) {
            case CENTER:
                return x - ((int) width >> 1);
            case RIGHT:
                return x - width;
            case LEFT:
            default:
                return x;
        }
    }

    private float transformYWithBaseline(float y, float height, Baseline baseline) {
        switch (baseline) {
            case MIDDLE:
                return y - ((int) height >> 1);
            case BOTTOM:
                return y - height;
            case TOP:
            default:
                return y;
        }
    }

    private float getOffsetX(float width, Alignment alignment) {
        switch (alignment) {
            case CENTER:
                return -((int) width >> 1);
            case RIGHT:
                return -width;
            case LEFT:
            default:
                return 0;
        }
    }

    private float getOffsetY(float height, Baseline baseline) {
        switch (baseline) {
            case MIDDLE:
                return -((int) height >> 1) * 1.5f;
            case BOTTOM:
                return -height;
            case TOP:
            default:
                return 0;
        }
    }

    public void drawStringScaled(String text, int givenX, int givenY, int color, double givenScale) {
        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        drawString(text, 0, 0, color);
        GL11.glPopMatrix();
    }

    public int drawString(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, Baseline.TOP, Alignment.LEFT);
    }

    public int drawString(String text, float x, float y, int color, Baseline baseline, Alignment alignment) {
        if (text == null) return 0;

        if (module == null) {
            module = (DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks");
        }

        if (resolution == null) {
            resolution = new ScaledResolution(Minecraft.getMinecraft());

            try {
                prevScaleFactor = resolution.getScaleFactor();
                unicodeFont = new UnicodeFont(getFontByName(name).deriveFont(size * prevScaleFactor / 2));
                unicodeFont.addAsciiGlyphs();
                unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
                unicodeFont.loadGlyphs();
            } catch (FontFormatException | IOException | SlickException e) {
                e.printStackTrace();
            }
        }

        this.antiAliasingFactor = resolution.getScaleFactor();

        if (!module.unicodeFontRendererEnabled) {
            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            int height = fr.FONT_HEIGHT;
            int width = fr.getStringWidth(text);
            Minecraft.getMinecraft().fontRendererObj.drawString(text, (int) transformXWithAlignment(x, width, alignment), (int) transformYWithBaseline(y, height, baseline), color);
            return 0;
        }

        GL11.glPushMatrix();
        GlStateManager.translate(getOffsetX(getWidth(text), alignment), getOffsetY(getHeight(text), baseline), 0);
        GlStateManager.scale(1 / antiAliasingFactor, 1 / antiAliasingFactor, 1 / antiAliasingFactor);
        x *= antiAliasingFactor;
        y *= antiAliasingFactor;

        float originalX = transformXWithAlignment(x, getWidth(text), alignment);
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);

        int currentColor = color;

        char[] characters = text.toCharArray();

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        String[] parts = COLOR_CODE_PATTERN.split(text);
        int index = 0;
        for (String s : parts) {
            for (String s2 : s.split("\n")) {
                for (String s3 : s2.split("\r")) {
                    unicodeFont.drawString(x, y, s3, new org.newdawn.slick.Color(currentColor));
                    x += unicodeFont.getWidth(s3);

                    index += s3.length();
                    if (index  < characters.length && characters[index ] == '\r') {
                        x = originalX;
                        index++;
                    }
                }
                if (index < characters.length && characters[index] == '\n') {
                    x = originalX;
                    y += getHeight(s2) * 2;
                    index++;
                }
            }
            if (index < characters.length) {
                char colorCode = characters[index];
                if (colorCode == '§') {
                    char colorChar = characters[index + 1];
                    int codeIndex = ("0123456789" +
                            "abcdef").indexOf(colorChar);
                    if (codeIndex < 0) {
                        if (colorChar == 'r') {
                            currentColor = color;
                        }
                    } else {
                        currentColor = colorCodes[codeIndex];
                    }
                    index += 2;
                }
            }
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.bindTexture(0);
        GlStateManager.popMatrix();
        return (int) x;
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        drawString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, 0x000000);
        return drawString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - ((int) getWidth(text) >> 1), y, color);
    }

    /**
     * Draw Centered Text Scaled
     *
     * @param text       - Given Text String
     * @param givenX     - Given X Position
     * @param givenY     - Given Y Position
     * @param color      - Given Color (HEX)
     * @param givenScale - Given Scale
     */
    public void drawCenteredTextScaled(String text, int givenX, int givenY, int color, double givenScale) {
        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        drawCenteredString(text, 0, 0, color);
        GL11.glPopMatrix();
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        drawCenteredString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, color);
        drawCenteredString(text, x, y, color);
    }

    public float getWidth(String text) {
        if (module == null) {
            module = (DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks");
        }
        if (resolution == null) {
            resolution = new ScaledResolution(Minecraft.getMinecraft());

            try {
                prevScaleFactor = resolution.getScaleFactor();
                unicodeFont = new UnicodeFont(getFontByName(name).deriveFont(size * prevScaleFactor / 2));
                unicodeFont.addAsciiGlyphs();
                unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
                unicodeFont.loadGlyphs();
            } catch (FontFormatException | IOException | SlickException e) {
                e.printStackTrace();
            }

            this.antiAliasingFactor = resolution.getScaleFactor();
        }
        if (!module.unicodeFontRendererEnabled) {
            return (float) Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
        }
        if (cachedStringWidth.size() > 1000) cachedStringWidth.clear();
        return cachedStringWidth.computeIfAbsent(text, e ->
                unicodeFont.getWidth(ColorUtil.stripColor(text)) / antiAliasingFactor
        );
    }

    public float getCharWidth(char c) {
        return unicodeFont.getWidth(String.valueOf(c));
    }

    public float getHeight(String s) {
        if (module == null) {
            module = (DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks");
        }
        if (!module.unicodeFontRendererEnabled) {
            return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
        }
        return unicodeFont.getHeight(s) / 2.0F;
    }

    public UnicodeFont getFont() {
        return unicodeFont;
    }

    public void drawSplitString(List<String> lines, int x, int y, int color) {
        drawString(
                String.join("\n\r", lines),
                x,
                y,
                color
        );
    }

    public List<String> splitString(String text, int wrapWidth) {
        List<String> lines = new ArrayList<>();

        String[] splitText = text.split(" ");
        StringBuilder currentString = new StringBuilder();

        for (String word : splitText) {
            String potential = currentString + " " + word;

            if (getWidth(potential) >= wrapWidth) {
                lines.add(currentString.toString());
                currentString = new StringBuilder();
            }

            currentString.append(word).append(" ");
        }

        lines.add(currentString.toString());
        return lines;
    }
}