package com.palight.playerinfo.mixin.client.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InventoryEffectRenderer.class)
public class MixinInventoryEffectRenderer extends GuiContainer {
    @Shadow private boolean hasActivePotionEffects;

    private DisplayTweaksMod module;

    public MixinInventoryEffectRenderer(Container p_i1072_1_) {
        super(p_i1072_1_);
    }

    /**
     * @reason Disable inventory GUI shifting when the player has potion effects
     * @author palight
     */
    @Overwrite
    protected void updateActivePotionEffects() {
        if (module == null) {
            module = (DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks");
        }
        boolean hasVisibleEffect = false;

        for (PotionEffect effect : this.mc.thePlayer.getActivePotionEffects()) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (potion.shouldRender(effect)) {
                hasVisibleEffect = true;
                break;
            }
        }

        if (!this.mc.thePlayer.getActivePotionEffects().isEmpty() && hasVisibleEffect) {
            if (module.disableInventoryShift) {
                this.guiLeft = (this.width - this.xSize) / 2;
            } else {
                this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
            }
            this.hasActivePotionEffects = true;
        } else {
            this.guiLeft = (this.width - this.xSize) / 2;
            this.hasActivePotionEffects = false;
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {

    }
}
