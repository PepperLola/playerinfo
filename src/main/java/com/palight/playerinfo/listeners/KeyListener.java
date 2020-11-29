package com.palight.playerinfo.listeners;

import com.palight.playerinfo.gui.screens.impl.MainGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyListener {

    public static String MOD_CATEGORY = "Player Info";

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        KeyBinding[] keybinds = Minecraft.getMinecraft().gameSettings.keyBindings;

        for (KeyBinding keybind : keybinds) {
            if (!keybind.getKeyCategory().equals(MOD_CATEGORY)) continue;
            if (!keybind.isKeyDown()) continue;

            String key_id = keybind.getKeyDescription();
            System.out.println(key_id);
            if (key_id.equals("key.main")) {
                MainGui.openGui();
            }
        }
    }
}
