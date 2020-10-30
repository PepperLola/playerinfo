package net.themorningcompany.playerinfo.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.palight.playerinfo.commands.FOVCommand.customFOV;

public class FovListener {
    @SubscribeEvent
    public void setFOV(EntityViewRenderEvent.FOVModifier e) {
        if (customFOV != -1) {
            e.setFOV(customFOV);
        }
    }

    @SubscribeEvent
    public void keyInputEvent(InputEvent.KeyInputEvent e) {
        String description = "key.zoom";
        KeyBinding[] keyBindings = Minecraft.getInstance().gameSettings.keyBindings;

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
