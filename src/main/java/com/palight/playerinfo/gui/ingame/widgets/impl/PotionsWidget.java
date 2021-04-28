package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.gui.PotionsMod;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class PotionsWidget extends GuiIngameWidget {
    protected static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");

    private PotionsMod module;

    public PotionsWidget() {
        super(0, -1, 140, 64);
    }

    @Override
    public void render(Minecraft mc) {
        if (module == null) {
            module = (PotionsMod) getModule();
        }
        if (this.getPosition().getY() == -1) {
            ScaledResolution res = new ScaledResolution(mc);
            this.getPosition().setY((res.getScaledHeight() - this.height) / 2);
        }
        Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();

        if (collection.isEmpty() && this.getState() == WidgetEditingState.EDITING) {
            collection = Arrays.asList(
                    new PotionEffect(1, 1), // swiftness
                    new PotionEffect(10, 1), // regeneration
                    new PotionEffect(22, 1) // absorption
            );

            collection.forEach(effect -> effect.setPotionDurationMax(true));
        }

        if (!collection.isEmpty()) {
            int itemHeight = 33;
            if (collection.size() > 5) {
                itemHeight = 132 / (collection.size() - 1);
            }

            this.height = collection.size() * itemHeight;
            super.render(mc);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();

            Iterator potionEffectIterator = collection.iterator();

            int yPosition = this.getPosition().getY();
            int xPosition = this.getPosition().getX();

            while(potionEffectIterator.hasNext()) {
                PotionEffect effect = (PotionEffect)potionEffectIterator.next();
                Potion potion = Potion.potionTypes[effect.getPotionID()];

                if (potion.shouldRender(effect)) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    mc.getTextureManager().bindTexture(inventoryBackground);

                    if (module.renderBackground) {
                        this.drawTexturedModalRect(xPosition, yPosition, 0, 166, 140, 32);
                    }

                    if (potion.hasStatusIcon()) {
                        int iconIndex = potion.getStatusIconIndex();
                        this.drawTexturedModalRect(xPosition + 6, yPosition + 7, iconIndex % 8 * 18, 198 + iconIndex / 8 * 18, 18, 18);
                    }

                    potion.renderInventoryEffect(xPosition, yPosition, effect, mc);

                    if (potion.shouldRenderInvText(effect)) {
                        String formattedName = I18n.format(potion.getName());
                        if (module.renderLevelAsNumber) {
                            formattedName += " " + (effect.getAmplifier() + 1);
                        } else {
                            formattedName += " " + NumberUtil.integerToRoman(effect.getAmplifier() + 1);
                        }

                        this.drawText(formattedName, xPosition + 10 + 18, yPosition + 6);
                        String durationString = Potion.getDurationString(effect);
                        this.drawText(durationString, xPosition + 10 + 18, yPosition + 6 + 10);
                    }

                    yPosition += itemHeight;
                }
            }
        }
    }
}
