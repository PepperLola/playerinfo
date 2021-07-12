package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Gui.class)
public interface IMixinGui {
    @Invoker
    void callDrawCenteredString(FontRenderer fr, String text, int x, int y, int color);
}
