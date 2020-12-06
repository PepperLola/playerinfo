package com.palight.playerinfo.modules;

import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public abstract class Module {
    private String id;
    private ModuleType type;
    private String name;
    private String description;
    protected boolean enabled;
    protected CustomGuiScreen optionsGui;

    protected GuiIngameWidget widget;

    /**
     * Constructor for module class
     * @param id Unique ID for the module.
     * @param name Module name that will be displayed in the menu.
     * @param description Module description. Also will be displayed in the menu.
     * @param type Module type.
     * @param optionsGui Options GUI. If not null, an options button will display in the menu.
     * @param widget Module widget. If not null, the widget will be rendered in-game.
     */
    public Module(String id, String name, String description, ModuleType type, @Nullable CustomGuiScreen optionsGui, GuiIngameWidget widget) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.optionsGui = optionsGui;
        this.widget = widget;
        MinecraftForge.EVENT_BUS.register(this);
        if (this.getWidget() != null) {
            this.getWidget().setModule(this);
        }
    }

    /**
     * Override with this.setEnabled with the appropriate ModConfiguration parameter
     */
    public abstract void init();

    /**
     * Starts editing the widget (e.g. moving it around). Most widgets display differently in editing mode than they do in-game.
     */
    public void startEditingWidgets() {
        if (widget == null) return;
        widget.startEditing();
    }

    /**
     * Stops editing the widget. Most widgets display differently in editing mode than they do in-game.
     */
    public void stopEditingWidgets() {
        if (widget == null) return;
        widget.stopEditing();
    }

    /**
     * Getter method for widget.
     * @return Module widget.
     */
    public GuiIngameWidget getWidget() {
        return widget;
    }

    /**
     * Sets widget directly.
     * @param widget Module widget.
     */
    public void setWidget(GuiIngameWidget widget) {
        this.widget = widget;
    }

    /**
     * Tests if module is enabled.
     * @return Module enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets if widget is enabled.
     * @param enabled Whether the module is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Method to enable the module without directly using setEnabled.
     */
    public void enable() {
        setEnabled(true);
    }

    /**
     * Method to disable the module without directly using setEnabled.
     */
    public void disable() {
        setEnabled(false);
    }

    /**
     * Gets module ID.
     * @return Module ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Set ID of module.
     * @param id Unique ID of the module.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets module type.
     * @return Module type.
     */
    public ModuleType getType() {
        return type;
    }

    /**
     * Sets module type directly.
     * @param type Module type.
     */
    public void setType(ModuleType type) {
        this.type = type;
    }

    /**
     * Gets module name.
     * @return Module name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets module name directly.
     * @param name Module name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets module description.
     * @return Module description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets module description directly.
     * @param description Module description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets module options GUI.
     * @return Module options GUI.
     */
    public CustomGuiScreen getOptionsGui() {
        return optionsGui;
    }

    /**
     * Sets module options GUI directly.
     * @param optionsGui Options GUI screen for the module.
     */
    public void setOptionsGui(CustomGuiScreen optionsGui) {
        this.optionsGui = optionsGui;
    }

    /**
     * Possible module types.
     */
    public static enum ModuleType {
        ANIMATION,
        MOVEMENT,
        GUI,
        UTIL,
        MISC
    }
}
