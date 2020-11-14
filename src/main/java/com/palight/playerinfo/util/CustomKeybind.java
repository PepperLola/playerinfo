package com.palight.playerinfo.util;

import net.minecraft.client.settings.KeyBinding;

public class CustomKeybind {
    private boolean pressed;
    private KeyBinding keybind;

    public CustomKeybind(KeyBinding keybind) {
        this(keybind, false);
    }

    public CustomKeybind(KeyBinding keybind, boolean pressed) {
        this.pressed = pressed;
        this.keybind = keybind;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public KeyBinding getKeybind() {
        return keybind;
    }

    public void setKeybind(KeyBinding keybind) {
        this.keybind = keybind;
    }
}
