package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.util.MCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ResourceWidget extends GuiIngameWidget {

    private final ResourceLocation overlayLocation = new ResourceLocation(PlayerInfo.MODID, "textures/overlay/bedwars_resources.png");

    public ResourceWidget() {
        super(-1, -1, 32, 64);
    }

    @Override
    public void render(Minecraft mc) {
        super.render(mc);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ScaledResolution res = new ScaledResolution(mc);
        int defaultX = 4;
        int defaultY = res.getScaledHeight() / 2 - this.height / 2;
        this.xPosition = xPosition == -1 ? defaultX : xPosition;
        this.yPosition = yPosition == -1 ? defaultY : yPosition;

        int iron = MCUtil.getTotalItems(player.inventory, Item.getByNameOrId("iron_ingot"));
        int gold = MCUtil.getTotalItems(player.inventory, Item.getByNameOrId("gold_ingot"));
        int diamond = MCUtil.getTotalItems(player.inventory, Item.getByNameOrId("diamond"));
        int emerald = MCUtil.getTotalItems(player.inventory, Item.getByNameOrId("emerald"));

        mc.getTextureManager().bindTexture(overlayLocation);
        this.drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);

        this.drawText(String.valueOf(iron), xPosition + 24 - mc.fontRendererObj.getStringWidth(String.valueOf(iron)) / 2, yPosition + 8 - mc.fontRendererObj.FONT_HEIGHT / 2);
        this.drawText(String.valueOf(gold), xPosition + 24 - mc.fontRendererObj.getStringWidth(String.valueOf(gold)) / 2, yPosition + 24 - mc.fontRendererObj.FONT_HEIGHT / 2);
        this.drawText(String.valueOf(diamond), xPosition + 24 - mc.fontRendererObj.getStringWidth(String.valueOf(diamond)) / 2, yPosition + 40 - mc.fontRendererObj.FONT_HEIGHT / 2);
        this.drawText(String.valueOf(emerald), xPosition + 24 - mc.fontRendererObj.getStringWidth(String.valueOf(emerald)) / 2, yPosition + 56 - mc.fontRendererObj.FONT_HEIGHT / 2);
    }
}