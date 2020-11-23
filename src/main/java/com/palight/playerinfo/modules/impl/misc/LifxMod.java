package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.LifxGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LifxMod extends Module {
    public LifxMod() {
        super("lifx", "Lifx", "Provides a LIFX integration", ModuleType.MISC, new LifxGui(), null);
    }

    @Override
    public void init() {
        this.setEnabled(ModConfiguration.lifxModEnabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_MODS, "lifxModEnabled", enabled);
        ModConfiguration.syncFromGUI();
        super.setEnabled(enabled);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer clientPlayer = Minecraft.getMinecraft().thePlayer;
        if (clientPlayer == null) return;
        if (!ModConfiguration.lifxModEnabled) return;

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
