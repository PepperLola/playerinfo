package com.palight.playerinfo.gui.dynamic.components;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.dynamic.DefaultUIConfig;
import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.util.NumberUtil;
import com.palight.playerinfo.util.math.Vector2;

import java.util.Arrays;

public class GuiDropdown extends DynamicGuiComponent {

    private boolean expanded = false;
    private int baseHeight;
    private String[] options;
    private String[] displayStrings;

    public GuiDropdown(DynamicGuiScreen screen, Vector2<Integer> position, Vector2<Integer> size) {
        super(screen, position, size);
        this.baseHeight = size.y;
    }

    public GuiDropdown(DynamicGuiScreen screen, int x, int y, int width, int height) {
        this(screen, new Vector2<>(x, y), new Vector2<>(width, height));
    }

    public GuiDropdown(DynamicGuiScreen screen, int x, int y) {
        this(screen, new Vector2<>(x, y), new Vector2<>(0, 0));
    }

    public GuiDropdown(DynamicGuiScreen screen) {
        this(screen, new Vector2<>(0, 0), new Vector2<>(0, 0));
    }

    public GuiDropdown setOptions(String[] options) {
        this.options = options;
        this.displayStrings = Arrays.copyOf(options, options.length);
        return this;
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {
        super.onClick(mouseX, mouseY, mouseButton);

        int localX = mouseX + this.screen.topLeft.x - this.position.x;
        int localY = mouseY + this.screen.topLeft.y - this.position.y;

        int selected = localY / baseHeight; // integer division
        if (expanded) {
            // don't reorder the options so the dropdown order remains consistent
            displayStrings = NumberUtil.prependElement(options, displayStrings[selected]);
        }

        this.expanded = !this.expanded;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        int height = this.size.y;

        if (expanded) this.size.y = baseHeight * options.length;
        else this.size.y = baseHeight;

        this.drawGradientRect(this.position.x, this.position.y, this.position.x + this.size.x, this.position.y + height, DefaultUIConfig.DEFAULT_COMPONENT_BACKGROUND_COLOR, DefaultUIConfig.DEFAULT_COMPONENT_BACKGROUND_COLOR);
//        this.renderBackground();

        if (this.isHovered) {
            int hovered = (mouseY - this.position.y) / baseHeight;
            this.drawGradientRect(this.position.x, this.position.y + hovered * baseHeight, this.position.x + this.size.x, this.position.y + (hovered + 1) * baseHeight, DefaultUIConfig.DEFAULT_COMPONENT_BACKGROUND_COLOR_HOVER, DefaultUIConfig.DEFAULT_COMPONENT_BACKGROUND_COLOR_HOVER);
        }

        int yPos = 0;
        for (String s : this.displayStrings) {
            PlayerInfo.instance.fontRendererObj.drawString(s, this.position.x + 2, this.position.y + yPos + 2, DefaultUIConfig.DEFAULT_LABEL_COLOR);
            if (!this.expanded) break; // only render the first option when rendering
            yPos += baseHeight;
        }
    }
}
