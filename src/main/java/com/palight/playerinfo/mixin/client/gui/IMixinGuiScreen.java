package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(GuiScreen.class)
public interface IMixinGuiScreen {
    @Invoker void callDrawHoveringText(List<String> text, int x, int y, FontRenderer fontRenderer);
}
