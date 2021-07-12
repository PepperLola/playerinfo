package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.client.gui.GuiResourcePackList;
import net.minecraft.client.renderer.Tessellator;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiResourcePackList.class)
public class MixinGuiResourcePackList extends MixinGuiListExtended {
    @Override
    protected void overlayBackground(int p_overlayBackground_1_, int p_overlayBackground_2_, int p_overlayBackground_3_, int p_overlayBackground_4_) {
        // do nothing
    }

    @Override
    protected void drawContainerBackground(Tessellator p_drawContainerBackground_1_) {
        // do nothing
    }
}
