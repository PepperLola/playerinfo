package com.palight.playerinfo.gui.dynamic.components;

import com.palight.playerinfo.gui.dynamic.DefaultUIConfig;
import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.rendering.font.UnicodeFontRenderer;
import com.palight.playerinfo.util.math.Vector2;

public class GuiCheckbox extends DynamicGuiComponent {
    private boolean enabled;
    private boolean disabled; // whether or not the user is allowed to toggle
    private GuiLabel label;

    public GuiCheckbox(DynamicGuiScreen screen, Vector2<Integer> position, Vector2<Integer> size) {
        super(screen, position, size);

        this.label = new GuiLabel(screen, position.x + size.x + 4, position.y/* + size.y / 2*/, 0, size.y)
            .setBaseline(UnicodeFontRenderer.Baseline.TOP);
    }

    public GuiCheckbox(DynamicGuiScreen screen, int x, int y, int width, int height) {
        this(screen, new Vector2<>(x, y), new Vector2<>(width, height));
    }

    public GuiCheckbox(DynamicGuiScreen screen, int x, int y) {
        this(screen, new Vector2<>(x, y), new Vector2<>(0, 0));
    }

    public GuiCheckbox(DynamicGuiScreen screen) {
        this(screen, new Vector2<>(0, 0), new Vector2<>(0, 0));
    }

    public GuiCheckbox setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public GuiCheckbox setLabel(String label) {
        this.label.setText(label);
        return this;
    }

    public GuiLabel getLabel() {
        return this.label;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public GuiCheckbox setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public <T extends DynamicGuiComponent> T setPosition(Vector2<Integer> position) {
        this.label.setPosition(position.x + this.size.x + 4, position.y + this.size.y / 2);
        return super.setPosition(position);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {
        if (this.disabled) {
            return;
        }

        this.enabled = !this.enabled;
        super.onClick(mouseX, mouseY, mouseButton);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.label.render(mouseX, mouseY, partialTicks);

        if (this.isEnabled()) {
            this.drawGradientRect(this.position.x + 4, this.position.y + 4, this.position.x + this.size.x - 4, this.position.y + this.size.y - 4, DefaultUIConfig.DEFAULT_LABEL_COLOR, DefaultUIConfig.DEFAULT_LABEL_COLOR);
        }
    }
}
