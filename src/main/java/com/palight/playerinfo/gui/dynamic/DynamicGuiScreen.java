package com.palight.playerinfo.gui.dynamic;

import com.palight.playerinfo.gui.dynamic.components.*;
import com.palight.playerinfo.util.math.Vector2;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class DynamicGuiScreen extends GuiScreen {
    protected Vector2<Integer> size = new Vector2<>(512, 472);
    public Vector2<Integer> topLeft = new Vector2<>(0, 0);

    private List<DynamicGuiComponent> components = new ArrayList<>();

    public DynamicGuiScreen() {
    }

    public DynamicGuiScreen(Vector2<Integer> size) {
        this();
        this.size = size;
    }

    public abstract void setup();

    protected void renderBackground() {
        this.drawGradientRect(topLeft.x, topLeft.y, topLeft.x + size.x, topLeft.y + size.y, DefaultUIConfig.DEFAULT_BACKGROUND_COLOR, DefaultUIConfig.DEFAULT_BACKGROUND_COLOR);
    }



    @Override
    public void initGui() {
        super.initGui();
        topLeft.x = (this.width - size.x) / 2;
        topLeft.y = (this.height - size.y) / 2;
        setup();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        super.mouseClicked(mouseX, mouseY, btn);

        for (DynamicGuiComponent component : components) {
            if (component.mouseIsOver(mouseX, mouseY)) {
                component.onClick(mouseX - topLeft.x, mouseY - topLeft.y, btn);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderBackground();

        this.components.forEach(component -> {
            component.setHovered(component.mouseIsOver(mouseX, mouseY));
            component.render(mouseX, mouseY, partialTicks);
        });
    }

    public void addComponent(DynamicGuiComponent component) {
        this.components.add(component);
    }

    public GuiStack createStack(int x, int y, int spacing) {
        GuiStack stack = new GuiStack(this, x + topLeft.x, y + topLeft.y).setSpacing(spacing);
        addComponent(stack);
        return stack;
    }

    public GuiSpacer createSpacer(int x, int y, int width, int height) {
        GuiSpacer spacer = new GuiSpacer(this, x + topLeft.x, y + topLeft.y, width, height);
        addComponent(spacer);
        return spacer;
    }

    public GuiLabel createLabel(String text, int x, int y) {
        GuiLabel label = new GuiLabel(this, x + topLeft.x, y + topLeft.y).setText(text);
        addComponent(label);
        return label;
    }

    public GuiButton createButton(String text, int x, int y, int width, int height) {
        GuiButton button = new GuiButton(this, x + topLeft.x, y + topLeft.y, width, height).setText(text);
        addComponent(button);
        return button;
    }

    public GuiCheckbox createCheckbox(String label, int x, int y, int width, int height, boolean checked) {
        GuiCheckbox checkbox = new GuiCheckbox(this, x + topLeft.x, y + topLeft.y, width, height).setLabel(label).setEnabled(checked);
        addComponent(checkbox);
        return checkbox;
    }
}
