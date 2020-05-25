package com.palight.playerinfo.gui;

import com.palight.playerinfo.gui.screens.InfoGui;
import com.palight.playerinfo.gui.screens.LoginGui;
import com.palight.playerinfo.gui.screens.MainGui;
import com.palight.playerinfo.gui.screens.integrations.IntegrationSelector;
import com.palight.playerinfo.gui.screens.integrations.lifx.LifxGui;
import com.palight.playerinfo.gui.screens.options.GuiOptions;
import com.palight.playerinfo.gui.screens.servers.ServerSelector;
import com.palight.playerinfo.gui.screens.servers.hypixel.BedwarsGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static int MAIN_GUI_ID = 0;
    public static int SETTINGS_GUI_ID = 1;
    public static int INFO_GUI_ID = 2;
    public static int LOGIN_GUI_ID = 3;
    public static int SERVER_GUI_ID = 4;
    public static int HYPIXEL_GUI_ID = 5;
    public static int INTEGRATION_GUI_ID = 6;
    public static int LIFX_GUI_ID = 7;

    @Override
    public Object getServerGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        if (id == MAIN_GUI_ID) {
            return new MainGui();
        } else if (id == INFO_GUI_ID) {
            return new InfoGui();
        } else if (id == SERVER_GUI_ID) {
            return new ServerSelector();
        } else if (id == HYPIXEL_GUI_ID) {
            return new BedwarsGui();
        } else if (id == INTEGRATION_GUI_ID) {
            return new IntegrationSelector();
        } else if (id == LIFX_GUI_ID) {
            return new LifxGui();
        } else if (id == SETTINGS_GUI_ID) {
            return new GuiOptions();
        } else if (id == LOGIN_GUI_ID) {
            return new LoginGui();
        }

        return null;
    }
}
