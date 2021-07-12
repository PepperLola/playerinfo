package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiScreenResourcePacks.class)
public class MixinGuiScreenResourcePacks extends MixinGuiScreen {
    @Shadow private GuiResourcePackAvailable availableResourcePacksList;

    @Shadow private GuiResourcePackSelected selectedResourcePacksList;

    /**
     * @author palight
     * @reason Remove resource pack list background
     */
    @Overwrite
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        FontRenderer fr = ((IMixinGuiScreen) this).getFontRendererObj();
        int width = ((IMixinGuiScreen) this).getWidth();
        int height = ((IMixinGuiScreen) this).getHeight();
//        ((IMixinGuiScreen) this).callDrawBackground(0);
        ((IMixinGuiScreen) this).callDrawDefaultBackground();
        this.availableResourcePacksList.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        this.selectedResourcePacksList.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        ((IMixinGui) this).callDrawCenteredString(fr, I18n.format("resourcePack.title"), width / 2, 16, 16777215);
        ((IMixinGui) this).callDrawCenteredString(fr, I18n.format("resourcePack.folderInfo"), width / 2 - 77, height - 26, 8421504);

        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }
}
