package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArmorWidget extends GuiIngameWidget {
    public ArmorWidget() {
        super(-1, -1, 48, 64);
    }

    @Override
    public void render(Minecraft mc) {
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        int offset = 0;
        List<ItemStack> items = new ArrayList<>(Arrays.asList(mc.thePlayer.inventory.armorInventory));
        if (this.getState() == WidgetEditingState.EDITING) {
            items = Arrays.asList(new ItemStack(Items.diamond_boots), new ItemStack(Items.diamond_leggings), new ItemStack(Items.diamond_chestplate), new ItemStack(Items.diamond_helmet));
        }
        int nonNullItems = 0;
        for (ItemStack is : items) {
            nonNullItems += is == null ? 0 : 1;
        }
        if (nonNullItems == 0) {
            return;
        }
        super.render(mc);
        Collections.reverse(items);
        for (ItemStack is : items) {
            if (is == null) continue;
            mc.getRenderItem().renderItemAndEffectIntoGUI(is, this.getPosition().getX(), this.getPosition().getY() + offset);
            if (is.isItemStackDamageable()) {
                double damage = ((is.getMaxDamage() - is.getItemDamage()) / (double) is.getMaxDamage()) * 100;
                String damageString = String.format("%.0f%%", damage);
                this.drawText(damageString, this.getPosition().getX() + 16 + (this.width - 16 - mc.fontRendererObj.getStringWidth(damageString)) / 2, this.getPosition().getY() + offset + (16 - mc.fontRendererObj.FONT_HEIGHT) / 2);
            }
            offset += 16;
        }
        GL11.glPopMatrix();
    }
}
