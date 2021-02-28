package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.LifxGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LifxMod extends Module {

    @ConfigOption
    public String lifxToken = "";
    @ConfigOption
    public String lifxSelector = "";
    @ConfigOption
    public boolean lifxTeamMode = false;

    public LifxMod() {
        super("lifx", "Lifx", "Provides a LIFX integration", ModuleType.MISC, new LifxGui(), null);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer clientPlayer = Minecraft.getMinecraft().thePlayer;
        if (clientPlayer == null) return;
        if (!enabled) return;

        ItemStack helmet = clientPlayer.getCurrentArmor(3);

        if (helmet == null) return;

        if (helmet.getItem() instanceof ItemArmor) {
            ItemArmor helmetArmor = (ItemArmor) helmet.getItem();
            if (helmetArmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                int color = helmetArmor.getColor(helmet);
                LifxGui.setTeamColor(color);
            }
        }
    }
}
