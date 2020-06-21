package com.palight.playerinfo.options;

import com.palight.playerinfo.PlayerInfo;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries.*;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ModConfiguration {
    private static Configuration config;
    private static File file;

    public static final String CATEGORY_GENERAL = Configuration.CATEGORY_GENERAL;

    public static final String CATEGORY_SERVERS = "servers";
    public static final String CATEGORY_HYPIXEL = CATEGORY_SERVERS + ".hypixel";

    public static final String CATEGORY_INTEGRATIONS = "integrations";
    public static final String CATEGORY_LIFX = CATEGORY_INTEGRATIONS + ".lifx";

    public static final String CATEGORY_GUI = "gui";

    public static final String CATEGORY_MODS = "mods";

    private static class DefaultValues {
        private static final boolean enableBlur = true;
        private static final boolean pumpkinOverlayDisabled = false;
        private static final boolean noteBlockHelper = false;

        private static final String selectedServer = "Hypixel";
        private static final String bedwarsMode = "bedwars_eight_one";

        private static final String lifxToken = "";
        private static final boolean lifxTeamMode = false;

        private static final boolean lifxModEnabled = false;
        private static final boolean scoreboardModEnabled = false;

        private static final boolean scoreboardEnabled = true;
        private static final boolean scoreboardNumbersEnabled = true;
    }

    public static boolean enableBlur = DefaultValues.enableBlur;
    public static boolean pumpkinOverlayDisabled = DefaultValues.pumpkinOverlayDisabled;
    public static boolean noteBlockHelper = DefaultValues.noteBlockHelper;

    public static String selectedServer = DefaultValues.selectedServer;
    public static String bedwarsMode = DefaultValues.bedwarsMode;

    public static String lifxToken = DefaultValues.lifxToken;
    public static boolean lifxTeamMode = DefaultValues.lifxTeamMode;

    public static boolean lifxModEnabled = DefaultValues.lifxModEnabled;
    public static boolean scoreboardModEnabled = DefaultValues.scoreboardModEnabled;

    public static boolean scoreboardEnabled = DefaultValues.scoreboardEnabled;
    public static boolean scoreboardNumbersEnabled = DefaultValues.scoreboardNumbersEnabled;

    public static Configuration getConfig() {
        return config;
    }

    public static void initConfig() throws IOException {
        file = new File(Loader.instance().getConfigDir(), PlayerInfo.MODID + ".cfg");

        if (!file.exists()) {
            file.createNewFile();
        }

        config = new Configuration(file);
        config.load();

        syncFromFile();
    }

    public static void syncFromFile() {
        syncConfig(true, true);
    }

    public static void syncFromGUI() {
        syncConfig(false, true);
    }

    private static void syncConfig(boolean loadConfigFromFile, boolean readFieldsFromConfig) {

        if (loadConfigFromFile) config.load();

        Property enableBlur = config.get(CATEGORY_GENERAL, "enableBlur", DefaultValues.enableBlur, "Enable background blurring in gui");
//        propStandardLevel.setLanguageKey("uncrafting.options.standardLevel");

        Property pumpkinOverlayDisabled = config.get(CATEGORY_GENERAL, "pumpkinOverlayDisabled", DefaultValues.pumpkinOverlayDisabled, "Disable pumpkin overlay");

        Property noteBlockHelper = config.get(CATEGORY_GENERAL, "noteBlockHelper", DefaultValues.noteBlockHelper, "Show note block notes");

        List<String> propOrderGeneral = new ArrayList<String>();
        propOrderGeneral.add(enableBlur.getName());
        propOrderGeneral.add(pumpkinOverlayDisabled.getName());
        propOrderGeneral.add(noteBlockHelper.getName());
        config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrderGeneral);

        Property scoreboardEnabled = config.get(CATEGORY_GUI, "scoreboardEnabled", DefaultValues.scoreboardEnabled, "Toggle scoreboard");
        Property scoreboardNumbersEnabled = config.get(CATEGORY_GUI, "scoreboardNumbersEnabled", DefaultValues.scoreboardNumbersEnabled, "Toggle the red numbers on the scoreboard");

        List<String> propOrderGui = new ArrayList<String>();
        propOrderGui.add(scoreboardEnabled.getName());
        propOrderGui.add(scoreboardNumbersEnabled.getName());
        config.setCategoryPropertyOrder(CATEGORY_GUI, propOrderGui);

        Property selectedServer = config.get(CATEGORY_SERVERS, "selectedServer", DefaultValues.selectedServer, "Selected server for server util");

        List<String> propOrderServers = new ArrayList<String>();
        propOrderServers.add(selectedServer.getName());
        config.setCategoryPropertyOrder(CATEGORY_SERVERS, propOrderServers);

        Property bedwarsMode = config.get(CATEGORY_HYPIXEL, "bedwarsMode", DefaultValues.bedwarsMode, "Bedwars mode for quick play");

        List<String> propOrderHypixel = new ArrayList<String>();
        propOrderHypixel.add(bedwarsMode.getName());
        config.setCategoryPropertyOrder(CATEGORY_HYPIXEL, propOrderHypixel);

        Property lifxToken = config.get(CATEGORY_LIFX, "lifxToken", DefaultValues.lifxToken, "LIFX token for LIFX integration");
        Property lifxTeamMode = config.get(CATEGORY_LIFX, "lifxTeamMode", DefaultValues.lifxTeamMode, "Whether or not the light should change based on your helmet color");

        List<String> propOrderLifx = new ArrayList<String>();
        propOrderLifx.add(lifxToken.getName());
        propOrderLifx.add(lifxTeamMode.getName());
        config.setCategoryPropertyOrder(CATEGORY_LIFX, propOrderLifx);

        Property lifxModEnabled = config.get(CATEGORY_MODS, "lifxModEnabled", DefaultValues.lifxModEnabled, "Enable LIFX Mod");
        Property scoreboardModEnabled = config.get(CATEGORY_MODS, "scoreboardModEnabled", DefaultValues.scoreboardModEnabled, "Enable scoreboard mod");

        List<String> propOrderMods = new ArrayList<String>();
        propOrderMods.add(scoreboardModEnabled.getName());
        propOrderMods.add(lifxModEnabled.getName());
        config.setCategoryPropertyOrder(CATEGORY_MODS, propOrderMods);

        try {
            enableBlur.setConfigEntryClass(BooleanEntry.class);
            pumpkinOverlayDisabled.setConfigEntryClass(BooleanEntry.class);
            noteBlockHelper.setConfigEntryClass(BooleanEntry.class);

            selectedServer.setConfigEntryClass(StringEntry.class);

            bedwarsMode.setConfigEntryClass(StringEntry.class);

            lifxToken.setConfigEntryClass(StringEntry.class);
            lifxTeamMode.setConfigEntryClass(BooleanEntry.class);

            lifxModEnabled.setConfigEntryClass(BooleanEntry.class);
            scoreboardModEnabled.setConfigEntryClass(BooleanEntry.class);

            scoreboardEnabled.setConfigEntryClass(BooleanEntry.class);
            scoreboardNumbersEnabled.setConfigEntryClass(BooleanEntry.class);
        } catch(NoClassDefFoundError e) {
            e.printStackTrace();
        }


        if (readFieldsFromConfig) {
            ModConfiguration.enableBlur = enableBlur.getBoolean();
            ModConfiguration.pumpkinOverlayDisabled = pumpkinOverlayDisabled.getBoolean();
            ModConfiguration.noteBlockHelper = noteBlockHelper.getBoolean();

            ModConfiguration.selectedServer = selectedServer.getString();

            ModConfiguration.bedwarsMode = bedwarsMode.getString();

            ModConfiguration.lifxToken = lifxToken.getString();
            ModConfiguration.lifxTeamMode = lifxTeamMode.getBoolean();

            ModConfiguration.lifxModEnabled = lifxModEnabled.getBoolean();
            ModConfiguration.scoreboardModEnabled = scoreboardModEnabled.getBoolean();

            ModConfiguration.scoreboardEnabled = scoreboardEnabled.getBoolean();
            ModConfiguration.scoreboardNumbersEnabled = scoreboardNumbersEnabled.getBoolean();
        }


        enableBlur.set(ModConfiguration.enableBlur);
        pumpkinOverlayDisabled.set(ModConfiguration.pumpkinOverlayDisabled);
        noteBlockHelper.set(ModConfiguration.noteBlockHelper);

        selectedServer.set(ModConfiguration.selectedServer);

        bedwarsMode.set(ModConfiguration.bedwarsMode);

        lifxToken.set(ModConfiguration.lifxToken);
        lifxTeamMode.set(ModConfiguration.lifxTeamMode);

        lifxModEnabled.set(ModConfiguration.lifxModEnabled);
        scoreboardModEnabled.set(ModConfiguration.scoreboardModEnabled);

        scoreboardEnabled.set(ModConfiguration.scoreboardEnabled);
        scoreboardNumbersEnabled.set(ModConfiguration.scoreboardNumbersEnabled);

        if (config.hasChanged()) config.save();
    }

    public static int getInt(String category, String key) {
        config = new Configuration(file);
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, 0).getInt();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return 0;
    }

    public static double getDouble(String category, String key) {
        config = new Configuration(file);
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, 0D).getDouble();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return 0D;
    }

    public static float getFloat(String category, String key) {
        config = new Configuration(file);
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return (float)config.get(category, key, 0D).getDouble();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return 0f;
    }

    public static String getString(String category, String key) {
        config = new Configuration(file);
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, "").getString();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return "";
    }

    public static short getShort(String category, String key) {
        config = new Configuration(file);
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return (short)config.get(category, key, (short)0).getInt();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return (short)0;
    }

    public static byte getByte(String category, String key) {
        config = new Configuration(file);
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return (byte)config.get(category, key, (byte)0).getInt();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return (byte)0;
    }

    public static boolean getBoolean(String category, String key) {
        config = new Configuration(file);
        try {
            config.load();
            if (config.getCategory(category).containsKey(key))
                return config.get(category, key, false).getBoolean();
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return false;
    }

    public static void writeConfig(String category, String key, String value) {
        config = new Configuration(file);
        try {
            config.load();
            String set = config.get(category, key, value).getString();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public static void writeConfig(String category, String key, int value) {
        config = new Configuration(file);
        try {
            config.load();
            int set = config.get(category, key, value).getInt();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public static void writeConfig(String category, String key, boolean value) {
        config = new Configuration(file);
        try {
            config.load();
            boolean set = config.get(category, key, value).getBoolean();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public static void writeConfig(String category, String key, double value) {
        config = new Configuration(file);
        try {
            config.load();
            double set = config.get(category, key, value).getDouble();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public static void writeConfig(String category, String key, short value) {
        config = new Configuration(file);
        try {
            config.load();
            int set = config.get(category, key, value).getInt();
            config.getCategory(category).get(key).set(Integer.valueOf(value));
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public static void writeConfig(String category, String key, byte value) {
        config = new Configuration(file);
        try {
            config.load();
            int set = config.get(category, key, value).getInt();
            config.getCategory(category).get(key).set(Integer.valueOf(value));
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public static void writeConfig(String category, String key, float value) {
        config = new Configuration(file);
        try {
            config.load();
            double set = config.get(category, key, value).getDouble();
            config.getCategory(category).get(key).set(Double.valueOf(value));
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public static boolean hasCategory(String category) {
        config = new Configuration(file);
        try {
            config.load();
            return config.hasCategory(category);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return false;
    }

    public static boolean hasKey(String category, String key) {
        config = new Configuration(file);
        try {
            config.load();
            if (!config.hasCategory(category))
                return false;
            return config.getCategory(category).containsKey(key);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return false;
    }
}
