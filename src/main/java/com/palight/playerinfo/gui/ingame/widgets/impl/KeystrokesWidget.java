package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.gui.KeystrokesMod;
import com.palight.playerinfo.modules.impl.misc.CPSMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class KeystrokesWidget extends GuiIngameWidget {

    public enum KeystrokesMode {
        WASD(Key.W, Key.A, Key.S, Key.D),
        WASD_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB),
        WASD_SPRINT(Key.W, Key.A, Key.S, Key.D, new Key("Sprint", () -> Minecraft.getMinecraft().gameSettings.keyBindSprint, 1, 41, 58, 18)),
        WASD_SPRINT_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB, new Key("Sprint", () -> Minecraft.getMinecraft().gameSettings.keyBindSprint, 1, 61, 58, 18));

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

        interface KeybindLambda {
            KeyBinding getKeybinding();
        }

        private static final Key W = new Key("W", () -> Minecraft.getMinecraft().gameSettings.keyBindForward, 21, 1, 18, 18);
        private static final Key A = new Key("A", () -> Minecraft.getMinecraft().gameSettings.keyBindLeft, 1, 21, 18, 18);
        private static final Key S = new Key("S", () -> Minecraft.getMinecraft().gameSettings.keyBindBack, 21, 21, 18, 18);
        private static final Key D = new Key("D", () -> Minecraft.getMinecraft().gameSettings.keyBindRight, 41, 21, 18, 18);

        private static final Key LMB = new Key("LMB", () -> Minecraft.getMinecraft().gameSettings.keyBindAttack, 1, 41, 28, 18);
        private static final Key RMB = new Key("RMB", () -> Minecraft.getMinecraft().gameSettings.keyBindUseItem, 31, 41, 28, 18);

        private final String name;
        private KeyBinding keyBinding;
        private final KeybindLambda keybindLambda;
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        public Key(String name, KeybindLambda keybindLambda, int x, int y, int width, int height) {
            this.name = name;
            this.keybindLambda = keybindLambda;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean isDown() {
            return getKeyBinding().isKeyDown();
        }

        public String getName() {
            return name;
        }

        public KeyBinding getKeyBinding() {
            if (keyBinding == null) {
                keyBinding = keybindLambda.getKeybinding();
            }
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

    private ScaledResolution res;

    public KeystrokesWidget() {
        super(-1, -1, 59, 59);
    }

    public KeystrokesMode getMode() {
        return mode;
    }

    public void setMode(KeystrokesMode mode) {
        this.mode = mode;
    }

    @Override
    public void render(Minecraft mc) {
        if (res == null) {
            res = new ScaledResolution(Minecraft.getMinecraft());

            if (this.getPosition().getX() == -1) {
                this.getPosition().setX(res.getScaledWidth() - this.width - 4);
            }

            if (this.getPosition().getY() == -1) {
                this.getPosition().setY(res.getScaledHeight() - this.height - 4);
            }
        }

        GL11.glPushMatrix();

        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);

        GlStateManager.disableBlend();

        for (Key key : mode.getKeys()) {
            int textWidth = (int) PlayerInfo.instance.fontRendererObj.getWidth(key.getName());

            Color color = key.isDown() ? new Color(255, 255, 255, 102) : new Color(0, 0, 0, 102);

            if (getModule().shouldRenderBackground() || key.isDown()) {
                Gui.drawRect(
                        this.getPosition().getX() + key.getX(),
                        this.getPosition().getY() + key.getY(),
                        this.getPosition().getX() + key.getX() + key.getWidth(),
                        this.getPosition().getY() + key.getY() + key.getHeight(),
                        color.getRGB()
                );
            }

            KeystrokesMod keystrokesMod = (KeystrokesMod) PlayerInfo.getModules().get("keystrokes");

            boolean showCPS = keystrokesMod.showCPS;
            boolean isMouseKey = key.name.equals("LMB") || key.name.equals("RMB");

            int offset = 0;
            if (showCPS && isMouseKey) offset = -4;
            this.drawTextVerticallyCentered(
                    key.getName(),
                    this.getPosition().getX() + key.getX() + (key.getWidth() - textWidth) / 2,
                    this.getPosition().getY() + key.getY() + key.getHeight() / 2 + offset,
                    key.isDown()
            );

            if (showCPS && isMouseKey) {
//                GlStateManager.scale(0.25f, 0.25f, 0.25f);
                String text = (key.name.equals("LMB") ? CPSMod.getLeftClicks() : CPSMod.getRightClicks()) + " CPS";
                textWidth = (int) PlayerInfo.instance.fontRendererObj.getWidth(text);
                this.drawText(
                        text,
                        this.getPosition().getX() + key.getX() + (key.getWidth() - textWidth) / 2,
                        this.getPosition().getY() + key.getY() + key.getHeight() / 2,
                        0.8f
                );
//                GlStateManager.scale(4f, 4f, 4f);
            }
        }

        if (blend) {
            GL11.glEnable(GL11.GL_BLEND);
        }

        GL11.glPopMatrix();
    }
}
