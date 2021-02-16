package com.palight.playerinfo.modules.impl.movement;

import com.palight.playerinfo.gui.ingame.widgets.impl.ToggleSprintWidget;
import com.palight.playerinfo.gui.screens.impl.options.modules.movement.ToggleSprintGui;
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
    private static boolean sneakingToggled = false;

    private static KeyBinding sprintKey = Minecraft.getMinecraft().gameSettings.keyBindSprint;
    private static KeyBinding sneakKey = Minecraft.getMinecraft().gameSettings.keyBindSneak;

    public ToggleSprintMod() {
        super("toggleSprint", "Toggle Sprint", "Toggle sprint and sneak", ModuleType.MOVEMENT, new ToggleSprintGui(), new ToggleSprintWidget(-1, -1));
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

        if (sprintingToggled) {
            KeyBinding.setKeyBindState(sprintKey.getKeyCode(), true);
        }

        if (ModConfiguration.toggleSneakModEnabled) {
            if (sneakKey.isPressed()) {
                sneakingToggled = !sneakingToggled;
            }

            if (sneakingToggled) {
                KeyBinding.setKeyBindState(sneakKey.getKeyCode(), true);
            }
        }
    }

    public static boolean isSprintingToggled() {
        return sprintingToggled;
    }
    public static boolean isSneakingToggled() {
        return sneakingToggled;
    }
}
