package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class KeystrokesWidget extends GuiIngameWidget {

    public enum KeystrokesMode {
        WASD(Key.W, Key.A, Key.S, Key.D),
        WASD_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB),
        WASD_SPRINT(Key.W, Key.A, Key.S, Key.D, new Key("Sprint", Minecraft.getMinecraft().gameSettings.keyBindSprint, 1, 41, 58, 18)),
        WASD_SPRINT_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB, new Key("Sprint", Minecraft.getMinecraft().gameSettings.keyBindSprint, 1, 61, 58, 18));

        private final Key[] keys;
        private int width;
        private int height;

        KeystrokesMode(Key... keys) {
            this.keys = keys;

            for (Key key : keys) {
                this.width = Math.max(this.width, key.getX() + key.getWidth());
                this.height = Math.max(this.height, key.getY() + key.getHeight());
            }
        }

        public Key[] getKeys() {
            return keys;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public static KeystrokesMode fromString(String s) {
            for (KeystrokesMode mode : KeystrokesMode.values()) {
                if (mode.toString().toUpperCase().equals(s)) {
                    return mode;
                }
            }

            return null;
        }
    }

    private static class Key {

        private static final Key W = new Key("W", Minecraft.getMinecraft().gameSettings.keyBindForward, 21, 1, 18, 18);
        private static final Key A = new Key("A", Minecraft.getMinecraft().gameSettings.keyBindLeft, 1, 21, 18, 18);
        private static final Key S = new Key("S", Minecraft.getMinecraft().gameSettings.keyBindBack, 21, 21, 18, 18);
        private static final Key D = new Key("D", Minecraft.getMinecraft().gameSettings.keyBindRight, 41, 21, 18, 18);

        private static final Key LMB = new Key("LMB", Minecraft.getMinecraft().gameSettings.keyBindAttack, 1, 41, 28, 18);
        private static final Key RMB = new Key("RMB", Minecraft.getMinecraft().gameSettings.keyBindUseItem, 31, 41, 28, 18);

        private final String name;
        private final KeyBinding keyBinding;
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        public Key(String name, KeyBinding keyBinding, int x, int y, int width, int height) {
            this.name = name;
            this.keyBinding = keyBinding;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean isDown() {
            return keyBinding.isKeyDown();
        }

        public String getName() {
            return name;
        }

        public KeyBinding getKeyBinding() {
            return keyBinding;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    private KeystrokesMode mode = KeystrokesMode.WASD_SPRINT_MOUSE;

    public KeystrokesWidget() {
        super(-1, -1, 59, 59);

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        this.getPosition().setX(res.getScaledWidth() - this.width - 4);
        this.getPosition().setY(res.getScaledHeight() - this.height - 4);
    }

    public KeystrokesMode getMode() {
        return mode;
    }

    public void setMode(KeystrokesMode mode) {
        this.mode = mode;
    }

    @Override
    public void render(Minecraft mc) {
        GL11.glPushMatrix();

        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);

        GL11.glDisable(GL11.GL_BLEND);

        for (Key key : mode.getKeys()) {
            int textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(key.getName());

            Gui.drawRect(
                    this.getPosition().getX() + key.getX(),
                    this.getPosition().getY() + key.getY(),
                    this.getPosition().getX() + key.getX() + key.getWidth(),
                    this.getPosition().getY() + key.getY() + key.getHeight(),
                    key.isDown() ? new Color(255, 255, 255, 102).getRGB() : new Color(0, 0, 0, 102).getRGB()
            );

            this.drawText(
                    key.getName(),
                    this.getPosition().getX() + key.getX() + (key.getWidth() - textWidth) / 2,
                    this.getPosition().getY() + key.getY() + key.getHeight() / 2 - 4,
                    key.isDown()
            );
        }

        if (blend) {
            GL11.glEnable(GL11.GL_BLEND);
        }

        GL11.glPopMatrix();
    }
}
