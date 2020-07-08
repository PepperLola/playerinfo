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

    private static KeyBinding forwardKey = Minecraft.getMinecraft().gameSettings.keyBindForward;
    private static KeyBinding sprintKey = Minecraft.getMinecraft().gameSettings.keyBindSprint;
    private static KeyBinding sneakKey = Minecraft.getMinecraft().gameSettings.keyBindSneak;

    public ToggleSprintMod() {
        super("toggleSprint", "Toggle Sprint", "Toggle sprint and sneak", ModuleType.MOVEMENT, new ToggleSprintGui(), new ToggleSprintWidget(2, 258));
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

        if (forwardKey.isKeyDown()) {
            if (sprintingToggled && !sneakKey.isKeyDown() && !player.isCollidedHorizontally) {
                player.setSprinting(true);
            }
        }

        if (!lastSprintState && sprintKey.isKeyDown()) {
            sprintingToggled = !sprintingToggled;
        }

        lastSprintState = sprintKey.isPressed();
    }
}
