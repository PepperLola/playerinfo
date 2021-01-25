package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
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
        if (Minecraft.getMinecraft().gameSettings.hideGUI) return;
        super.render(mc);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ScaledResolution res = new ScaledResolution(mc);
        int defaultX = 4;
        int defaultY = res.getScaledHeight() / 2 - this.height / 2;
        getPosition().setX(getPosition().getX() == -1 ? defaultX : getPosition().getX());
        getPosition().setY(getPosition().getY() == -1 ? defaultY : getPosition().getY());

        int iron = MCUtil.getTotalItems(player.inventory, Item.getByNameOrId("iron_ingot"));
        int gold = MCUtil.getTotalItems(player.inventory, Item.getByNameOrId("gold_ingot"));
        int diamond = MCUtil.getTotalItems(player.inventory, Item.getByNameOrId("diamond"));
        int emerald = MCUtil.getTotalItems(player.inventory, Item.getByNameOrId("emerald"));

        mc.getTextureManager().bindTexture(overlayLocation);
        this.drawTexturedModalRect(getPosition().getX(), getPosition().getY(), 0, 0, width, height);

        this.drawText(String.valueOf(iron), (int) (getPosition().getX() + 24 - PlayerInfo.instance.fontRendererObj.getWidth(String.valueOf(iron)) / 2), (int) (getPosition().getY() + 8 - PlayerInfo.instance.fontRendererObj.getHeight(String.valueOf(iron)) / 2));
        this.drawText(String.valueOf(gold), (int) (getPosition().getX() + 24 - PlayerInfo.instance.fontRendererObj.getWidth(String.valueOf(gold)) / 2), (int) (getPosition().getY() + 24 - PlayerInfo.instance.fontRendererObj.getHeight(String.valueOf(gold)) / 2));
        this.drawText(String.valueOf(diamond), (int) (getPosition().getX() + 24 - PlayerInfo.instance.fontRendererObj.getWidth(String.valueOf(diamond)) / 2), (int) (getPosition().getY() + 40 - PlayerInfo.instance.fontRendererObj.getHeight(String.valueOf(diamond)) / 2));
        this.drawText(String.valueOf(emerald), (int) (getPosition().getX() + 24 - PlayerInfo.instance.fontRendererObj.getWidth(String.valueOf(emerald)) / 2), (int) (getPosition().getY() + 56 - PlayerInfo.instance.fontRendererObj.getHeight(String.valueOf(emerald)) / 2));
    }
}