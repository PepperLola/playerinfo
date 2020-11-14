package com.palight.playerinfo.modules.movement;

import com.palight.playerinfo.gui.ingame.widgets.ToggleSprintWidget;
import com.palight.playerinfo.gui.screens.options.modules.movement.ToggleSprintGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToggleSprintMod extends Module {

    private static boolean sprintingToggled = false;
    private static boolean lastSprintState = false;

    private static KeyBinding sprintKey = Minecraft.getMinecraft().gameSettings.keyBindSprint;
    private static KeyBinding sneakKey = Minecraft.getMinecraft().gameSettings.keyBindSneak;

    public ToggleSprintMod() {
        super("toggleSprint", "Toggle Sprint", "Toggle sprint and sneak", ModuleType.MOVEMENT, new ToggleSprintGui(), new ToggleSprintWidget(-1, -1));
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.toggleSprintModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "toggleSprintModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (!this.isEnabled()) return;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        if (sprintKey.isPressed()) {
            sprintingToggled = !sprintingToggled;
        }

        System.out.println(sprintingToggled + " | " + sprintKey.isKeyDown() + " | " + sprintKey.isPressed());

        if (sprintingToggled) {
            KeyBinding.setKeyBindState(sprintKey.getKeyCode(), true);
        }
    }

    public static boolean isSprintingToggled() {
        return sprintingToggled;
    }
}
