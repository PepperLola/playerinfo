package com.palight.playerinfo.macro;

import com.palight.playerinfo.PlayerInfo;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class MacroConfig {
    private static Configuration config;
    private static File file;

    public static Map<String, Macro> macros = new HashMap<>();

    public static final String CATEGORY_MACROS = "macros";

    private static class DefaultValues {
        private static final String[] hypixelFriendJoin = new String[]{};
        private static final String[] messageReceive = new String[]{};
    }

    public static String[] hypixelFriendJoin = DefaultValues.hypixelFriendJoin;
    public static String[] messageReceive = DefaultValues.messageReceive;

    public static Configuration getConfig() {
        return config;
    }

    public static void initConfig() throws IOException {
        file = new File(Loader.instance().getConfigDir(), PlayerInfo.MODID + "_macro.cfg");

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

        Property hypixelFriendJoin = config.get(CATEGORY_MACROS, "hypixelFriendJoin", DefaultValues.hypixelFriendJoin, "Macro to run when one of your friends joins on Hypixel");
        Property messageReceive = config.get(CATEGORY_MACROS, "messageReceive", DefaultValues.messageReceive, "Macro to run when you receive a chat message");

        List<String> propOrderHypixel = new ArrayList<>();
        propOrderHypixel.addAll(Arrays.asList(
                hypixelFriendJoin.getName(),
                messageReceive.getName()
        ));
        config.setCategoryPropertyOrder(CATEGORY_MACROS, propOrderHypixel);

        try {
            hypixelFriendJoin.setConfigEntryClass(GuiConfigEntries.ArrayEntry.class);
            messageReceive.setConfigEntryClass(GuiConfigEntries.ArrayEntry.class);
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }

        if (readFieldsFromConfig) {
            MacroConfig.hypixelFriendJoin = hypixelFriendJoin.getStringList();
            MacroConfig.messageReceive = messageReceive.getStringList();

            config.getCategory(CATEGORY_MACROS).keySet().forEach(key -> {
                macros.put(key, new Macro(config.getCategory(CATEGORY_MACROS).get(key).getStringList()));
            });
        }

        hypixelFriendJoin.set(MacroConfig.hypixelFriendJoin);
        messageReceive.set(MacroConfig.messageReceive);

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

    public static String[] getStringList(String category, String key) {
        config = new Configuration(file);
        try {
            config.load();
            if (config.getCategory(category).containsKey(key))
                return config.get(category, key, new String[0]).getStringList();
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return new String[0];
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

    public static void writeConfig(String category, String key, String[] value) {
        config = new Configuration(file);
        try {
            config.load();
            String[] set = config.get(category, key, value).getStringList();
            config.getCategory(category).get(key).set(value);
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
