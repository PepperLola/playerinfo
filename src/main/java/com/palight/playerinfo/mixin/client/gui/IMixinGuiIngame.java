package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiIngame.class)
public interface IMixinGuiIngame {
    @Accessor String getDisplayedTitle();
    @Accessor String getDisplayedSubTitle();
}
