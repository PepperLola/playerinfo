package com.palight.playerinfo.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface IMixinMinecraft {
    @Invoker void callClickMouse();
    @Invoker void callRightClickMouse();
    @Invoker void callMiddleClickMouse();
}
