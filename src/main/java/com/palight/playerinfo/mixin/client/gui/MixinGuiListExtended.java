package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.client.gui.GuiListExtended;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiListExtended.class)
public class MixinGuiListExtended extends MixinGuiSlot {
}
