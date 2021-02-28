package com.palight.playerinfo.listeners;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.impl.MainMenuGui;
import com.palight.playerinfo.modules.impl.gui.CustomMainMenuMod;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MainScreenHandler {

    private CustomMainMenuMod module;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Pre event) {
        if (module == null) {
            module = ((CustomMainMenuMod) PlayerInfo.getModules().get("mainMenu"));
        }
        if (!module.isEnabled() || !(event.gui instanceof GuiMainMenu)) return;
        event.gui.mc.displayGuiScreen(new MainMenuGui());
    }
}
