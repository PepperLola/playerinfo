package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmorWidget extends GuiIngameWidget {
    public ArmorWidget() {
        super(-1, -1, 48, 64);
    }

    @Override
    public void render(Minecraft mc) {
        int offset = 0;
        List<ItemStack> items;
        if (this.getState() == WidgetEditingState.EDITING) {
            items = Arrays.asList(new ItemStack(Items.diamond_helmet), new ItemStack(Items.diamond_chestplate), new ItemStack(Items.diamond_leggings), new ItemStack(Items.diamond_boots));
        } else {
            items = new ArrayList<>(Arrays.asList(mc.thePlayer.inventory.armorInventory));
        }
        int nonNullItems = 0;
        for (ItemStack is : items) {
            nonNullItems += is == null ? 0 : 1;
        }
        if (nonNullItems == 0) {
            return;
        }
        super.render(mc);

        int i = 0;
        for (ItemStack is : items) {

            renderItemStack(this.getPosition().getX(), this.getPosition().getY() + offset, i, is);
            i++;
//            if (is == null) continue;
//            mc.getRenderItem().renderItemAndEffectIntoGUI(is, this.getPosition().getX(), this.getPosition().getY() + offset);
//            if (is.isItemStackDamageable()) {
//                double damage = ((is.getMaxDamage() - is.getItemDamage()) / (double) is.getMaxDamage()) * 100;
//                String damageString = String.format("%.0f%%", damage);
//                this.drawText(damageString, (int) Math.floor(this.getPosition().getX() + 16 + (this.width - 16 - PlayerInfo.instance.fontRendererObj.getWidth(damageString)) / 2), (int) (this.getPosition().getY() + offset + (16 - PlayerInfo.instance.fontRendererObj.getHeight(damageString)) / 2));
//            }
//            offset += 16;
        }

//        GlStateManager.color(1F, 1F, 1F);
    }

    private void renderItemStack(int x, int y, int i, ItemStack is) {
        if (is == null) return;

        GL11.glPushMatrix();

        int yAdd = this.height + (i * -16) - 16;

        if (is.isItemStackDamageable()) {
            double damage = ((is.getMaxDamage() - is.getItemDamage()) / (double) is.getMaxDamage()) * 100;
            String damageString = String.format("%.0f%%", damage);
            double stringWidth = PlayerInfo.instance.fontRendererObj.getWidth(damageString);
            double stringHeight = PlayerInfo.instance.fontRendererObj.getHeight(damageString);
            this.drawTextVerticallyCentered(
                    damageString,
                    (int) (x + 8 + (this.width - stringWidth) / 2),
                    y + yAdd + 8
            );
        }

        RenderHelper.enableGUIStandardItemLighting();

        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(is, x, y + yAdd);

        GL11.glPopMatrix();
    }
}
