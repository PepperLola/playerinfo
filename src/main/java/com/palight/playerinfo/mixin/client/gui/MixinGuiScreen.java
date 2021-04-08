package com.palight.playerinfo.mixin.client.gui;

import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {
    @Shadow protected FontRenderer fontRendererObj;
    /**
     * @reason Render enchantment levels > 10
     * @author palight
     */
    @Overwrite
    protected void renderToolTip(ItemStack item, int x, int y) {
        List<String> list = item.getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);

        for(int i = 0; i < list.size(); ++i) {
            String current = list.get(i);
            Pattern enchantmentPattern = Pattern.compile("enchantment.level.(\\d+)");
            Matcher matcher = enchantmentPattern.matcher(current);
            if (matcher.find()) {
                System.out.println("TOOLTIP TEXT: " + current + " | GROUP 0: " + matcher.group(0) + " | GROUP 1: " + matcher.group(1));
                int level = Integer.parseInt(matcher.group(1));
                current = current.replaceAll("enchantment.level.(\\d+)", NumberUtil.integerToRoman(level));
            }
            if (i == 0) {
                list.set(i, item.getRarity().rarityColor + current);
            } else {
                list.set(i, EnumChatFormatting.GRAY + current);
            }
        }

        FontRenderer font = item.getItem().getFontRenderer(item);
        ((IMixinGuiScreen) this).callDrawHoveringText(list, x, y, font == null ? this.fontRendererObj : font);
    }
}
