package com.palight.playerinfo.modules;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import net.minecraftforge.common.MinecraftForge;

public abstract class Module {
    private String id;
    private ModuleType type;
    private String name;
    private String description;
    protected boolean enabled;
    protected CustomGuiScreen optionsGui;

    public Module(String id, String name, String description, ModuleType type, CustomGuiScreen optionsGui) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.optionsGui = optionsGui;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public abstract void init();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void enable() {
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModuleType getType() {
        return type;
    }

    public void setType(ModuleType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomGuiScreen getOptionsGui() {
        return optionsGui;
    }

    public void setOptionsGui(CustomGuiScreen optionsGui) {
        this.optionsGui = optionsGui;
    }

    public static enum ModuleType {
        ANIMATION,
        MOVEMENT,
        GUI,
        UTIL,
        MISC
    }
}
