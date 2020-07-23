package com.palight.playerinfo;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.gui.*;
import com.palight.playerinfo.modules.misc.BlurMod;
import com.palight.playerinfo.modules.misc.CPSMod;
import com.palight.playerinfo.modules.misc.LifxMod;
import com.palight.playerinfo.modules.movement.ToggleSprintMod;
import com.palight.playerinfo.modules.util.NoteBlockMod;
import com.palight.playerinfo.proxy.CommonProxy;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mod(modid = PlayerInfo.MODID, version = PlayerInfo.VERSION)
public class PlayerInfo
{
    public static final String NAME = "playerinfo";
    public static final String MODID = "playerinfo";
    public static final String VERSION = "1.11.3";
    public static final String SERVER_PROXY_CLASS = "com.palight.playerinfo.proxy.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "com.palight.playerinfo.proxy.ClientProxy";
    public static String DATA_FOLDER;
    public static File configFile = new File(DATA_FOLDER + "config.json");

    public static Random random = new Random();

    public static Gson gson = new Gson();
    public static JsonParser parser = new JsonParser();

    @SidedProxy(serverSide = PlayerInfo.SERVER_PROXY_CLASS, clientSide = PlayerInfo.CLIENT_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.Instance("playerinfo")
    public static PlayerInfo instance;

    private static Map<String, Module> modules = new HashMap<String, Module>();

    static {
        modules.put("scoreboard", new ScoreboardMod());
        modules.put("lifx", new LifxMod());
        modules.put("backgroundBlur", new BlurMod());
        modules.put("pumpkinOverlay", new PumpkinMod());
        modules.put("noteBlockHelper", new NoteBlockMod());
        modules.put("coords", new CoordsMod());
        modules.put("mainMenu", new CustomMainMenuMod());
        modules.put("toggleSprint", new ToggleSprintMod());
        modules.put("resources", new BedwarsResourcesMod());
        modules.put("cps", new CPSMod());
        modules.put("displayTweaks", new DisplayTweaksMod());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("(PLAYERINFO) INITIALIZING MOD!");
        proxy.init(event);
        Arrays.stream(GuiConnecting.class.getDeclaredMethods()).forEach(method -> {
            System.out.println("(PLAYERINFO) " + method);
        });
        createDataFolder();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public void createDataFolder() {
        File dataFolder = new File(DATA_FOLDER);

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public static Map<String, Module> getModules() {
        return modules;
    }
}
