package com.palight.playerinfo;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.palight.playerinfo.listeners.FovListener;
import com.palight.playerinfo.listeners.HitListener;
import com.palight.playerinfo.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import org.apache.commons.io.FileUtils;
import scala.util.parsing.json.JSON;
import scala.util.parsing.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Mod(modid = PlayerInfo.MODID, version = PlayerInfo.VERSION)
public class PlayerInfo
{
    public static final String MODID = "playerinfo";
    public static final String VERSION = "1.0";
    public static final String SERVER_PROXY_CLASS = "com.palight.playerinfo.proxy.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "com.palight.playerinfo.proxy.ClientProxy";
    public static final String DATA_FOLDER = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/playerinfo/";
    public static File configFile = new File(DATA_FOLDER + "config.json");

    public static Gson gson = new Gson();
    public static JsonParser parser = new JsonParser();

    @SidedProxy(serverSide = PlayerInfo.SERVER_PROXY_CLASS, clientSide = PlayerInfo.CLIENT_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.Instance("playerinfo")
    public static PlayerInfo instance;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("(PLAYERINFO) INITIALIZING MOD!");
        proxy.init(event);
    }

    public static void createDataFolder() {
        File dataFolder = new File(DATA_FOLDER);
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public static void createConfig() {
        createDataFolder();

        Map<String, Object> startingConfig = new HashMap<String, Object>();

        startingConfig.put("SELECTED_SERVER", "Hypixel");

        Map<String, Object> servers = new HashMap<String, Object>();

        Map<String, Object> hypixel = new HashMap<String, Object>();

        hypixel.put("BEDWARS_MODE", "bedwars_eight_one");

        servers.put("Hypixel", hypixel);

        startingConfig.put("SERVERS", servers);

        Map<String, Object> lifx = new HashMap<String, Object>();

        lifx.put("TOKEN", "");
        lifx.put("TEAMS", false);

        startingConfig.put("LIFX", lifx);

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                FileUtils.write(configFile, gson.toJson(startingConfig));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static JsonObject loadConfig() {
        try {
            String data = FileUtils.readFileToString(configFile);
            return parser.parse(gson.toJson(data)).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object getConfigValue(String key) {
        try {
            String data = FileUtils.readFileToString(configFile);
            return gson.fromJson(parser.parse(data).getAsJsonObject().get(key), Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void addConfigValue(String key, Object value) {
        try {
            String data = FileUtils.readFileToString(configFile);

            Map<String, Object> values = gson.fromJson(data, HashMap.class);

            values.put(key, value);

            FileUtils.write(configFile, gson.toJson(values));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
