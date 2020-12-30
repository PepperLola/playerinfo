package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.PerspectiveGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class PerspectiveMod extends Module {

    private boolean mustHoldKey = ModConfiguration.mustHoldPerspectiveKey; // if true, the player must hold the key to stay in perspective mode, otherwise it'll toggle
    private boolean perspectiveToggled = false;

    private float cameraYaw = 0F;
    private float cameraPitch = 0F;

    private int previousPerspective = 0; // previous f5 state

    public PerspectiveMod() {
        super("perspective", "Perspective Mod", "Lets you change your camera orientation in F5 without rotating your character.", ModuleType.MISC, new PerspectiveGui(), null);
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.perspectiveModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "perspectiveModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    @SubscribeEvent
    public void onKeyEvent(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (Keyboard.getEventKey() == CommonProxy.keybinds.get("key.perspective").getKeyCode()) {
            if (Keyboard.getEventKeyState()) {
                perspectiveToggled = !perspectiveToggled;

                cameraYaw = mc.thePlayer.rotationYaw;
                cameraPitch = mc.thePlayer.rotationPitch;

                if (perspectiveToggled) {
                    previousPerspective = mc.gameSettings.thirdPersonView;
                    mc.gameSettings.thirdPersonView = 1;
                } else {
                    mc.gameSettings.thirdPersonView = previousPerspective;
                }
            } else if (mustHoldKey) {
                perspectiveToggled = false;
                mc.gameSettings.thirdPersonView = previousPerspective;
            }
        }

        if (Keyboard.getEventKey() == mc.gameSettings.keyBindTogglePerspective.getKeyCode()) {
            perspectiveToggled = false;
        }
    }

    public boolean isMustHoldKey() {
        return mustHoldKey;
    }

    public void setMustHoldKey(boolean mustHoldKey) {
        this.mustHoldKey = mustHoldKey;
    }

    public boolean isPerspectiveToggled() {
        return perspectiveToggled;
    }

    public float getCameraYaw() {
        return perspectiveToggled ? cameraYaw : Minecraft.getMinecraft().thePlayer.rotationYaw;
    }

    public float getCameraPitch() {
        return perspectiveToggled ? cameraPitch : Minecraft.getMinecraft().thePlayer.rotationPitch;
    }

    public void setCameraYaw(float cameraYaw) {
        this.cameraYaw = cameraYaw;
    }

    public void setCameraPitch(float cameraPitch) {
        this.cameraPitch = cameraPitch;
    }

    public int getPreviousPerspective() {
        return previousPerspective;
    }
}
