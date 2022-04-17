package com.palight.playerinfo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.gui.ingame.widgets.WidgetState;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.impl.gui.*;
import com.palight.playerinfo.modules.impl.misc.*;
import com.palight.playerinfo.modules.impl.movement.ToggleSprintMod;
import com.palight.playerinfo.modules.impl.util.NoteBlockMod;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.proxy.CommonProxy;
import com.palight.playerinfo.rendering.font.UnicodeFontRenderer;
import com.palight.playerinfo.util.ApiUtil;
import com.palight.playerinfo.util.HttpUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Mod(modid = PlayerInfo.MODID, version = PlayerInfo.VERSION)
public class PlayerInfo
{
    //TODO update version here and in build.gradle
    public static final String NAME = "playerinfo";
    public static final String MODID = "playerinfo";
    public static final String VERSION = "1.18.0";
    public static String commitHash;
    public static String defaultBranchName = "master";
    public static String githubAPIURL = "https://api.github.com/repos/PepperLola/playerinfo/git/refs/heads/" + defaultBranchName;
    public static final String SERVER_PROXY_CLASS = "com.palight.playerinfo.proxy.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "com.palight.playerinfo.proxy.ClientProxy";
    public static String DATA_FOLDER;
    public static String CONFIG_FILE;
    public static String TOKEN = "";

    public final UnicodeFontRenderer fontRendererObj = new UnicodeFontRenderer("robotosb", 16.0F);

    public static Random random = new Random();

    public static Gson gson = new Gson();
    public static JsonParser parser = new JsonParser();

    @SidedProxy(serverSide = PlayerInfo.SERVER_PROXY_CLASS, clientSide = PlayerInfo.CLIENT_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.Instance("playerinfo")
    public static PlayerInfo instance;

    private static final Map<String, Module> modules = new HashMap<>();

    static {
        HttpUtil.httpGet(githubAPIURL, response -> {
            HttpEntity entity = response.getEntity();
            String entityString = EntityUtils.toString(entity);
            JsonObject githubObj = parser.parse(entityString).getAsJsonObject();
            JsonObject objectObj = githubObj.get("object").getAsJsonObject();
            commitHash = objectObj.get("sha").getAsString();
        });

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
        modules.put("fps", new FPSMod());
        modules.put("reachDisplay", new ReachDisplayMod());
        modules.put("displayTweaks", new DisplayTweaksMod());
        modules.put("hypixelEvents", new HypixelEventsMod());
        modules.put("discordRPC", new DiscordRichPresenceMod());
        modules.put("ping", new PingMod());
        modules.put("particle", new ParticleMod());
        modules.put("armor", new ArmorMod());
        modules.put("perspective", new PerspectiveMod());
        modules.put("timeChanger", new TimeChangerMod());
        modules.put("oldAnimations", new OldAnimationsMod());
        modules.put("keystrokes", new KeystrokesMod());
        modules.put("textReplacement", new TextReplacementMod());
        modules.put("fullBright", new FullBrightMod());
        modules.put("stats", new StatsMod());
        modules.put("clock", new ClockMod());
        modules.put("memory", new MemoryMod());
        modules.put("potions", new PotionsMod());
        modules.put("screenshotHelper", new ScreenshotHelperMod());
        modules.put("playerHider", new PlayerHiderMod());
        modules.put("autogg", new AutoGGMod());
        modules.put("entityRenderTweaks", new EntityRenderTweaksMod());
        modules.put("combo", new ComboMod());
        modules.put("itemPhysics", new ItemPhysicsMod());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        System.out.println("(PLAYERINFO) INITIALIZING MOD!");
        proxy.init(event);
        createDataFolder();
        loadToken();
        startSendingOnline();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public void createDataFolder() throws IOException {
        File dataFolder = new File(DATA_FOLDER);

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File settingsFile = new File(CONFIG_FILE);
        if (!settingsFile.exists()) {
            settingsFile.createNewFile();
        }
    }

    public static Map<String, Module> getModules() {
        return modules;
    }

    public static void saveWidgetPositions() {
        Map<String, WidgetState> modulePositions = new HashMap<>();
        for (Module module : getModules().values()) {
            GuiIngameWidget widget = module.getWidget();
            if (module.getWidget() != null) {
                modulePositions.put(module.getId(), widget.getPosition());
            }
        }
        List<String> positions = new ArrayList<>();
        for (String moduleId : modulePositions.keySet()) {
            WidgetState state = modulePositions.get(moduleId);
            positions.add(state.toString());
        }
        ModConfiguration.writeConfig(ModConfiguration.CATEGORY_WIDGETS, "widgetStates", positions.toArray(new String[0]));
        ModConfiguration.syncFromGUI();
    }

    public void loadToken() throws IOException {
        File settingsFile = new File(CONFIG_FILE);
        String contents = FileUtils.readFileToString(settingsFile);
        JsonElement element = new JsonParser().parse(contents);
        if (element == null || element.isJsonNull()) return;
        JsonObject obj = element.getAsJsonObject();
        if (obj == null) return;
        JsonElement token = obj.get("token");
        if (token != null) {
            PlayerInfo.TOKEN = token.getAsString();
        }
    }

    public void setConfigValue(String key, String value) throws IOException {
        File settingsFile = new File(CONFIG_FILE);
        String contents = FileUtils.readFileToString(settingsFile);
        JsonElement element = new JsonParser().parse(contents);
        if (element.isJsonNull()) {
            element = new JsonObject();
        }
        element.getAsJsonObject().addProperty(key, value);
        FileUtils.write(settingsFile, element.toString());
    }

    public void startSendingOnline() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    ApiUtil.sendOnline();
                    ApiUtil.testToken();
                    Thread.sleep(30000); // keepalive every 30s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public static void setModuleStates() {
        for (String widgetState : ModConfiguration.widgetStates) {
            WidgetState state = WidgetState.fromString(widgetState);
            if (state != null) {
                state.getModule().setPosition(state);
            }
        }
    }
}
