package com.palight.playerinfo.proxy;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.commands.CalcCommand;
import com.palight.playerinfo.commands.TextTransformCommand;
import com.palight.playerinfo.gui.GuiHandler;
import com.palight.playerinfo.listeners.*;
import com.palight.playerinfo.macro.MacroConfig;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.rendering.cosmetics.Cape;
import com.palight.playerinfo.rendering.cosmetics.CapeHandler;
import com.palight.playerinfo.rendering.cosmetics.Cosmetics;
import com.palight.playerinfo.util.ApiUtil;
import com.palight.playerinfo.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Util;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.http.util.EntityUtils;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class CommonProxy {

    public static Map<String, KeyBinding> keybinds = new HashMap<>();

    public void preInit(FMLPreInitializationEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT) {
            throw new RuntimeException(String.format("%s (version %s) should only be run on the client!", PlayerInfo.MODID, PlayerInfo.VERSION));
        }
    }

    public void init(FMLInitializationEvent event) {

        for (Module module : PlayerInfo.getModules().values()) {
            MinecraftForge.EVENT_BUS.register(module);
        }

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
        ClientCommandHandler.instance.registerCommand(new TextTransformCommand());

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
        PlayerInfo.CONFIG_FILE = PlayerInfo.DATA_FOLDER + "config.json";

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

        // fetch capes
        new Thread(() -> {
            ApiUtil.getCosmetics(response -> {
                String entityString = EntityUtils.toString(response.getEntity());
                JsonElement element = new JsonParser().parse(entityString);
                JsonElement capes = element.getAsJsonObject().get("capes");
                JsonArray capesArray = capes.getAsJsonArray();

                for (JsonElement capeElement : capesArray) {
                    JsonObject capeObj = capeElement.getAsJsonObject();
                    String name = capeObj.get("name").getAsString();
                    int fps = capeObj.get("fps").getAsInt();
                    int frames = capeObj.get("frames").getAsInt();
                    boolean animated = capeObj.get("animated").getAsBoolean();

                    Cape cape = new Cape(name, frames, fps, animated);

                    Cosmetics.addCape(name, cape);
                }
            });
        }).start();
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
