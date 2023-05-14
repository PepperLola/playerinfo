package com.palight.playerinfo.modules;

import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.gui.dynamic.components.GuiStack;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.options.ConfigOption;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Module {
    private String id;
    private ModuleType type;
    @ConfigOption
    protected boolean enabled;

    @ConfigOption
    protected boolean renderBackground = true;

    protected CustomGuiScreen optionsGui;

    protected GuiIngameWidget widget;

    /**
     * Constructor for module class
     * @param id Unique ID for the module.
     * @param type Module type.
     * @param optionsGui Options GUI. If not null, an options button will display in the menu.
     * @param widget Module widget. If not null, the widget will be rendered in-game.
     */
    public Module(String id, ModuleType type, @Nullable CustomGuiScreen optionsGui, GuiIngameWidget widget) {
        this.id = id;
        this.type = type;
        this.optionsGui = optionsGui;
        this.widget = widget;
        if (this.getWidget() != null) {
            this.getWidget().setModule(this);
        }
    }

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
     * Gets module options GUI.
     * @return Module options GUI.
     */
    public DynamicGuiScreen getOptionsGui() {
        List<Field> options = Arrays.stream(getClass().getFields()).filter(field -> field.isAnnotationPresent(ConfigOption.class)).collect(Collectors.toList());
        return new DynamicGuiScreen("screen." + this.id) {

            @Override
            public void setup() {
                GuiStack stack = this.createStack(0, 0, 4);
                for (Field option : options) {
                    try {
                        if (Boolean.TYPE.equals(option.getType())) {
                            stack.addComponent(this.createCheckbox(option.getName(), 0, 0, 20, 20, option.getBoolean(Module.this)));
                        } else if (String.class.equals(option.getType())) {
                            stack.addComponent(this.createCheckbox(option.getName(), 0, 0, 20, 20, option.getBoolean(Module.this)));
                        } else if (Double.TYPE.equals(option.getType())) {
                            stack.addComponent(this.createCheckbox(option.getName(), 0, 0, 20, 20, option.getBoolean(Module.this)));
                        } else if (Integer.TYPE.equals(option.getType())) {
                            stack.addComponent(this.createCheckbox(option.getName(), 0, 0, 20, 20, option.getBoolean(Module.this)));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    /**
     * Sets module options GUI directly.
     * @param optionsGui Options GUI screen for the module.
     */
    public void setOptionsGui(CustomGuiScreen optionsGui) {
        this.optionsGui = optionsGui;
    }

    /**
     * Gets whether the module's widget should have a background
     * @return Whether the widget should render a background
     */
    public boolean shouldRenderBackground() {
        return renderBackground;
    }

    /**
     * Sets whether the widget should have a background
     * @param renderBackground Whether the widget should render a background
     */
    public void setRenderBackground(boolean renderBackground) {
        this.renderBackground = renderBackground;
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
