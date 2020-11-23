package com.palight.playerinfo.gui.screens.impl.options;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;

public class GuiCustomConfig extends net.minecraftforge.fml.client.config.GuiConfig {


    public GuiCustomConfig(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), PlayerInfo.MODID, false, false, PlayerInfo.NAME);
    }

    private static List<IConfigElement> getConfigElements() {
        Configuration config = ModConfiguration.getConfig();

        List<IConfigElement> list = new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements();

        list.add(new DummyCategoryElement(I18n.format("playerinfo.options.servers"), "playerinfo.options.servers", CategoryEntryServers.class));
        list.add(new DummyCategoryElement(I18n.format("playerinfo.options.hypixel"), "playerinfo.options.hypixel", CategoryEntryHypixel.class));
        list.add(new DummyCategoryElement(I18n.format("playerinfo.options.integrations"), "playerinfo.options.integrations", CategoryEntryIntegrations.class));
        list.add(new DummyCategoryElement(I18n.format("playerinfo.options.lifx"), "playerinfo.options.lifx", CategoryEntryLifx.class));

        return list;
    }

    public static class CategoryEntryServers extends GuiConfigEntries.CategoryEntry {

        public CategoryEntryServers(net.minecraftforge.fml.client.config.GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
        }

        @Override
        protected GuiScreen buildChildScreen() {
            Configuration configuration = ModConfiguration.getConfig();
            ConfigElement configurationCategory = new ConfigElement(configuration.getCategory(ModConfiguration.CATEGORY_SERVERS));
            List<IConfigElement> properties = configurationCategory.getChildElements();
            String windowTitle = I18n.format("playerinfo.options.servers");

            return new net.minecraftforge.fml.client.config.GuiConfig(this.owningScreen, properties,
                    this.owningScreen.modID,
                    ModConfiguration.CATEGORY_SERVERS,
                    this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                    this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                    windowTitle
            );
        }
    }

    public static class CategoryEntryHypixel extends GuiConfigEntries.CategoryEntry {

        public CategoryEntryHypixel(net.minecraftforge.fml.client.config.GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
        }

        @Override
        protected GuiScreen buildChildScreen() {
            Configuration configuration = ModConfiguration.getConfig();
            ConfigElement configurationCategory = new ConfigElement(configuration.getCategory(ModConfiguration.CATEGORY_HYPIXEL));
            List<IConfigElement> properties = configurationCategory.getChildElements();
            String windowTitle = I18n.format("playerinfo.options.hypixel");

            return new net.minecraftforge.fml.client.config.GuiConfig(this.owningScreen, properties,
                    this.owningScreen.modID,
                    ModConfiguration.CATEGORY_SERVERS,
                    this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                    this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                    windowTitle
            );
        }
    }

    public static class CategoryEntryIntegrations extends GuiConfigEntries.CategoryEntry {

        public CategoryEntryIntegrations(net.minecraftforge.fml.client.config.GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
        }

        @Override
        protected GuiScreen buildChildScreen() {
            Configuration configuration = ModConfiguration.getConfig();
            ConfigElement configurationCategory = new ConfigElement(configuration.getCategory(ModConfiguration.CATEGORY_INTEGRATIONS));
            List<IConfigElement> properties = configurationCategory.getChildElements();
            String windowTitle = I18n.format("playerinfo.options.integrations");

            return new net.minecraftforge.fml.client.config.GuiConfig(this.owningScreen, properties,
                    this.owningScreen.modID,
                    ModConfiguration.CATEGORY_SERVERS,
                    this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                    this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                    windowTitle
            );
        }
    }

    public static class CategoryEntryLifx extends GuiConfigEntries.CategoryEntry {

        public CategoryEntryLifx(net.minecraftforge.fml.client.config.GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
        }

        @Override
        protected GuiScreen buildChildScreen() {
            Configuration configuration = ModConfiguration.getConfig();
            ConfigElement configurationCategory = new ConfigElement(configuration.getCategory(ModConfiguration.CATEGORY_LIFX));
            List<IConfigElement> properties = configurationCategory.getChildElements();
            String windowTitle = I18n.format("playerinfo.options.lifx");

            return new net.minecraftforge.fml.client.config.GuiConfig(this.owningScreen, properties,
                    this.owningScreen.modID,
                    ModConfiguration.CATEGORY_SERVERS,
                    this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                    this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                    windowTitle
            );
        }
    }
}
