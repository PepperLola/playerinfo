package com.palight.playerinfo.listeners;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
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
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                World playerWorld = player.getEntityWorld();
                BlockPos playerLocation = player.getPosition();
                player.openGui(PlayerInfo.instance, GuiHandler.MAIN_GUI_ID, playerWorld, playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
            }
        }
    }
}
