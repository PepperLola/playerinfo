package com.palight.playerinfo.proxy;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.commands.CalcCommand;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.listeners.*;
import com.palight.playerinfo.macro.MacroConfig;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.rendering.CapeHandler;
import com.palight.playerinfo.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Util;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class CommonProxy {

    public static Map<String, KeyBinding> keybinds = new HashMap<>();

    public void init(FMLInitializationEvent event) {

        // Registering Listeners
        MinecraftForge.EVENT_BUS.register(PlayerInfo.instance);
        MinecraftForge.EVENT_BUS.register(new HitListener());
        MinecraftForge.EVENT_BUS.register(new KeyListener());
        MinecraftForge.EVENT_BUS.register(new RenderListener());
        MinecraftForge.EVENT_BUS.register(new FovListener());
        MinecraftForge.EVENT_BUS.register(new MainScreenHandler());
        MinecraftForge.EVENT_BUS.register(new ChatListener());
        MinecraftForge.EVENT_BUS.register(new MacroEventHandler());
        MinecraftForge.EVENT_BUS.register(new CapeHandler());

        // Registering Commands
        ClientCommandHandler.instance.registerCommand(new CalcCommand());

        // Registering Keybinds
        // key codes at https://computercraft.info/wiki/images/thumb/8/81/CC-Keyboard-Charcodes.png/963px-CC-Keyboard-Charcodes.png
        keybinds.put("key.zoom", new KeyBinding("key.zoom", 21, KeyListener.MOD_CATEGORY));
        keybinds.put("key.main", new KeyBinding("key.main", 35, KeyListener.MOD_CATEGORY));
        keybinds.put("key.perspective", new KeyBinding("key.perspective", 29, KeyListener.MOD_CATEGORY));

        keybinds.values().forEach(ClientRegistry::registerKeyBinding);

        // Register Gui Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(PlayerInfo.instance, new GuiHandler());

        // init config
        try {
            ModConfiguration.initConfig();
            MacroConfig.initConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MacroConfig.syncFromFile();

        PlayerInfo.DATA_FOLDER = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/playerinfo/";

        Display.setTitle("playerinfo v" + PlayerInfo.VERSION + " (" + PlayerInfo.commitHash.substring(0, 7) + "/" + PlayerInfo.defaultBranchName + ")");

        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (InputStream inputStream16x = Minecraft.class.getResourceAsStream("/assets/playerinfo/icons/icon-16x.png");
                 InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/playerinfo/icons/icon-32x.png")) {
                ByteBuffer[] icons = new ByteBuffer[]{RenderUtil.readImageToBuffer(inputStream16x), RenderUtil.readImageToBuffer(inputStream32x)};
                Display.setIcon(icons);
            } catch (Exception e) {
                System.err.println("Couldn't set Windows Icon " + e.getMessage());
            }
        }

        if (ModConfiguration.widgetStates.length == 0) {
            PlayerInfo.saveWidgetPositions();
        }
        PlayerInfo.setModuleStates();
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
