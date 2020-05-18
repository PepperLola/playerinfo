package com.palight.playerinfo.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class FovListener {

    public static int customFOV = -1;

    @SubscribeEvent
    public void setFOV(EntityViewRenderEvent.FOVModifier e) {
        if (customFOV != -1) {
            e.setFOV(customFOV);
        }
    }

    @SubscribeEvent
    public void keyInputEvent(InputEvent.KeyInputEvent e) {
        String description = "key.zoom";
        KeyBinding[] keyBindings = Minecraft.getMinecraft().gameSettings.keyBindings;

        for (KeyBinding keyBinding : keyBindings) {
            if (keyBinding.getKeyDescription().equals(description)) {
                if (keyBinding.isKeyDown()) {
                    customFOV = 1;
                } else {
                    customFOV = -1;
                }
            }
        }
    }
}
