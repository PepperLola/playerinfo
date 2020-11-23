package com.palight.playerinfo.listeners;

import com.palight.playerinfo.gui.screens.impl.MainMenuGui;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MainScreenHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Pre event) {
        if (!ModConfiguration.mainMenuModEnabled || !(event.gui instanceof GuiMainMenu)) return;
        event.gui.mc.displayGuiScreen(new MainMenuGui());
    }
}
