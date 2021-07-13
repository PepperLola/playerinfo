package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(GuiScreen.class)
public interface IMixinGuiScreen {
    @Invoker void callDrawHoveringText(List<String> text, int x, int y, FontRenderer fontRenderer);
    @Invoker void callDrawBackground(int i);
    @Invoker void callDrawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_);
    @Invoker void callDrawDefaultBackground();

    @Accessor int getWidth();
    @Accessor int getHeight();
    @Accessor FontRenderer getFontRendererObj();
}
