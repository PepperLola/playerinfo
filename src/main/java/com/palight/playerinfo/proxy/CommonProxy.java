package com.palight.playerinfo.proxy;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.commands.CalcCommand;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.listeners.*;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.IOException;

public class CommonProxy {
    public void init(FMLInitializationEvent event) {

        // Registering Listeners
        MinecraftForge.EVENT_BUS.register(new HitListener());
        MinecraftForge.EVENT_BUS.register(new CommandListener());
        MinecraftForge.EVENT_BUS.register(new KeyListener());
        MinecraftForge.EVENT_BUS.register(new RenderListener());
        MinecraftForge.EVENT_BUS.register(new FovListener());
        MinecraftForge.EVENT_BUS.register(new MainScreenHandler());

        // Registering Commands
        ClientCommandHandler.instance.registerCommand(new CalcCommand());

        // Registering Keybinds
        ClientRegistry.registerKeyBinding(new KeyBinding("key.zoom", 21, "Player Info"));
        ClientRegistry.registerKeyBinding(new KeyBinding("key.main", 35, "Player Info"));

        // Register Gui Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(PlayerInfo.instance, new GuiHandler());

        // init config
        try {
            ModConfiguration.initConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Module module : PlayerInfo.getModules().values()) {
            module.init();
        }

        PlayerInfo.DATA_FOLDER = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/playerinfo/";
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
