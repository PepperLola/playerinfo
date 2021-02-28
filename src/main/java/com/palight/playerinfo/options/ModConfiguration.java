package com.palight.playerinfo.options;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.util.ReflectionUtil;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.StringEntry;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class ModConfiguration {
    public static final String CATEGORY_GENERAL = Configuration.CATEGORY_GENERAL;
    public static final String CATEGORY_SERVERS = "servers";
    public static final String CATEGORY_HYPIXEL = CATEGORY_SERVERS + ".hypixel";
    public static final String CATEGORY_INTEGRATIONS = "integrations";
    public static final String CATEGORY_LIFX = CATEGORY_INTEGRATIONS + ".lifx";
    public static final String CATEGORY_GUI = "gui";
    public static final String CATEGORY_WIDGETS = "widgets";
    public static final String CATEGORY_TWEAKS = "tweaks";
    public static final String CATEGORY_DISPLAY = CATEGORY_TWEAKS + ".display";
    public static String[] widgetStates = DefaultValues.widgetStates;
    private static Configuration config;
    private static File file;

    public static void initConfig() throws IOException {
        if (config == null) {
            file = new File(Loader.instance().getConfigDir(), PlayerInfo.MODID + ".cfg");

            if (!file.exists()) {
                file.createNewFile();
            }

            config = new Configuration(file);
            config.load();

            syncFromFile();
        }
    }

    public static Configuration getConfig() {
        try {
            initConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static void syncFromFile() {
        Property widgetStates = config.get(CATEGORY_WIDGETS, "widgetStates", DefaultValues.widgetStates, "Saved widget states");
        ModConfiguration.widgetStates = widgetStates.getStringList();
        Set<String> categoryNames = config.getCategoryNames();

        for (String name : categoryNames) {
            Module module = null;
            for (Module storedModule : PlayerInfo.getModules().values()) {
                if (storedModule.getId().equals(name)) {
                    module = storedModule;
                    break;
                }
            }
            if (module == null) continue;
            ConfigCategory moduleCategory = config.getCategory(name);
            List<Property> properties = moduleCategory.getOrderedValues();
            List<Field> fields = ReflectionUtil.getAllFields(new ArrayList<>(), module.getClass());
            for (Field field : fields) {
                if (!field.isAnnotationPresent(ConfigOption.class)) continue;
                if (!field.isAccessible())
                    field.setAccessible(true);
                for (Property property : properties) {
                    if (field.getName().equals(property.getName())) {
                        try {
                            field.set(module, ModConfiguration.getValue(property));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void syncFromGUI() {
        Collection<Module> modules = PlayerInfo.getModules().values();

        for (Module module : modules) {
            List<Field> fields = ReflectionUtil.getAllFields(new ArrayList<>(), module.getClass());

            for (Field field : fields) {
                if (!field.isAccessible())
                    field.setAccessible(true);
                boolean hasAnnotation = field.isAnnotationPresent(ConfigOption.class);

                if (hasAnnotation) {
                    try {
                        setProperty(config, field, module);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static int getInt(String category, String key) {
        try {
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, 0).getInt();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!" + e.getMessage());
        } finally {
            config.save();
        }
        return 0;
    }

    public static double getDouble(String category, String key) {
        try {
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, 0D).getDouble();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!" + e.getMessage());
        } finally {
            config.save();
        }
        return 0D;
    }

    public static float getFloat(String category, String key) {
        try {
            if (config.getCategory(category).containsKey(key)) {
                return (float) config.get(category, key, 0D).getDouble();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!" + e.getMessage());
        } finally {
            config.save();
        }
        return 0f;
    }

    public static String getString(String category, String key) {
        try {
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, "").getString();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!" + e.getMessage());
        } finally {
            config.save();
        }
        return "";
    }

    public static short getShort(String category, String key) {
        try {
            if (config.getCategory(category).containsKey(key)) {
                return (short) config.get(category, key, (short) 0).getInt();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!" + e.getMessage());
        } finally {
            config.save();
        }
        return (short) 0;
    }

    public static byte getByte(String category, String key) {
        try {
            if (config.getCategory(category).containsKey(key)) {
                return (byte) config.get(category, key, (byte) 0).getInt();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!" + e.getMessage());
        } finally {
            config.save();
        }
        return (byte) 0;
    }

    public static boolean getBoolean(String category, String key) {
        try {
            if (config.getCategory(category).containsKey(key))
                return config.get(category, key, false).getBoolean();
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!" + e.getMessage());
        } finally {
            config.save();
        }
        return false;
    }

    public static String[] getStringList(String category, String key) {
        try {
            if (config.getCategory(category).containsKey(key))
                return config.get(category, key, new String[0]).getStringList();
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!" + e.getMessage());
        } finally {
            config.save();
        }
        return new String[0];
    }

    private static Property getProperty(String category, String key, Property.Type type) {
        ConfigCategory configCategory = config.getCategory(category);
        Property property = configCategory.get(key);
        if (property == null) {
            property = new Property(key, "", type);
            configCategory.put(key, property);
        }

        return property;
    }

    public static void writeConfig(String category, String key, String value) {
        Property property = getProperty(category, key, Property.Type.STRING);
        property.set(value);
    }

    public static void writeConfig(String category, String key, int value) {
        Property property = getProperty(category, key, Property.Type.INTEGER);
        property.set(value);
    }

    public static void writeConfig(String category, String key, boolean value) {
        Property property = getProperty(category, key, Property.Type.BOOLEAN);
        property.set(value);
    }

    public static void writeConfig(String category, String key, double value) {
        Property property = getProperty(category, key, Property.Type.DOUBLE);
        property.set(value);
    }

    public static void writeConfig(String category, String key, String[] value) {
        Property property = getProperty(category, key, Property.Type.STRING);
        property.setValues(value);
    }

    public static boolean hasCategory(String category) {
        try {
            return config.hasCategory(category);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!" + e.getMessage());
        } finally {
            config.save();
        }
        return false;
    }

    public static boolean hasKey(String category, String key) {
        try {
            if (!config.hasCategory(category))
                return false;
            return config.getCategory(category).containsKey(key);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!" + e.getMessage());
        } finally {
            config.save();
        }
        return false;
    }

    public static Object getValue(Property property) {
        switch (property.getType()) {
            case INTEGER:
                return property.getInt();
            case DOUBLE:
                return property.getDouble();
            case BOOLEAN:
                return property.getBoolean();
            case STRING:
            default:
                return property.getString();
        }
    }

    public static Class<? extends GuiConfigEntries.IConfigEntry> getConfigEntryClass(Field field) {
        if (Boolean.class.equals(field.getType())) {
            return GuiConfigEntries.BooleanEntry.class;
        } else if (Double.class.equals(field.getType())) {
            return GuiConfigEntries.DoubleEntry.class;
        } else if (String.class.equals(field.getType())) {
            return StringEntry.class;
        }

        return GuiConfigEntries.IntegerEntry.class;
    }

    public static void setProperty(Configuration config, Field field, Module module) throws IllegalAccessException {
        if (Boolean.TYPE.equals(field.getType())) {
            writeConfig(module.getId(), field.getName(), (boolean) field.get(module));
        } else if (String.class.equals(field.getType())) {
            writeConfig(module.getId(), field.getName(), String.valueOf(field.get(module)));
        } else if (Double.TYPE.equals(field.getType())) {
            writeConfig(module.getId(), field.getName(), (double) field.get(module));
        } else if (Integer.TYPE.equals(field.getType())) {
            writeConfig(module.getId(), field.getName(), (int) field.get(module));
        }

        config.save();
    }

    private static class DefaultValues {
        private static final String hypixelApiKey = "";
        private static final String[] widgetStates = new String[0];
    }
}
