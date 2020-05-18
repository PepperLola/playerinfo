package com.palight.playerinfo.listeners;

import com.palight.playerinfo.gui.screens.integrations.lifx.LifxGui;
import com.palight.playerinfo.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;

public class TickHandler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer clientPlayer = Minecraft.getMinecraft().thePlayer;
        if (clientPlayer == null) return;

        ItemStack helmet = clientPlayer.getCurrentArmor(3);

        if (helmet == null) return;

        if (helmet.getItem() instanceof ItemArmor) {
            ItemArmor helmetArmor = (ItemArmor) helmet.getItem();
            if (helmetArmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                int color = helmetArmor.getColor(helmet);
                System.out.println(color);
                LifxGui.setTeamColor(color);
            }
        }
    }
}
