package com.palight.playerinfo.mixin;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import net.minecraft.client.Minecraft;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow public int displayHeight;
    @Shadow public int displayWidth;
    @Shadow private boolean fullscreen;

    private final DisplayTweaksMod displayTweaksMod = ((DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks"));

    @Inject(method = "setInitialDisplayMode", at = @At(value = "HEAD"), cancellable = true)
    private void displayFix(CallbackInfo ci) throws LWJGLException {
        Display.setFullscreen(false);
        if (fullscreen) {
            if (displayTweaksMod.windowedFullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            } else {
                Display.setFullscreen(true);
                DisplayMode displaymode = Display.getDisplayMode();
                Minecraft.getMinecraft().displayWidth = Math.max(1, displaymode.getWidth());
                Minecraft.getMinecraft().displayHeight = Math.max(1, displaymode.getHeight());
            }
        } else {
            if (displayTweaksMod.windowedFullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
            } else {
                Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            }
        }

        Display.setResizable(false);
        Display.setResizable(true);

        // effectively overwrites the method
        ci.cancel();
    }

    @Inject(method = "toggleFullscreen", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/opengl/Display;setVSyncEnabled(Z)V", shift = At.Shift.AFTER))
    private void fullScreenFix(CallbackInfo ci) throws LWJGLException {
        if (displayTweaksMod.windowedFullscreen) {
            if (fullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                Display.setLocation(0, 0);
                Display.setFullscreen(false);
            } else {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            }
        } else {
            Display.setFullscreen(fullscreen);
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
        }

        Display.setResizable(false);
        Display.setResizable(true);
    }
}
