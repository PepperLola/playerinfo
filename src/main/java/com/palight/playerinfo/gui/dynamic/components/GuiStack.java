package com.palight.playerinfo.gui.dynamic.components;

import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.util.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiStack extends DynamicGuiComponent {

    private List<DynamicGuiComponent> components = new ArrayList<>();
    private Direction direction = Direction.VERTICAL;
    private int spacing = 0;

    public GuiStack(DynamicGuiScreen screen, Vector2<Integer> position, Vector2<Integer> size) {
        super(screen, position, size);
    }

    public GuiStack(DynamicGuiScreen screen, int x, int y, int width, int height) {
        this(screen, new Vector2<>(x, y), new Vector2<>(width, height));
    }

    public GuiStack(DynamicGuiScreen screen, int x, int y) {
        this(screen, x, y, 0, 0);
    }

    public GuiStack(DynamicGuiScreen screen) {
        this(screen, 0, 0);
    }

    private void arrangeComponents() {
        if (direction == Direction.VERTICAL) {
            AtomicInteger y = new AtomicInteger(this.position.y);
            this.components.forEach(component -> {
                component.setPosition(this.position.x, y.getAndAdd(component.size.y + spacing));
            });
        } else {
            AtomicInteger x = new AtomicInteger(this.position.x);
            this.components.forEach(component -> {
                component.setPosition(x.getAndAdd(spacing + component.size.x), this.position.y);
            });
        }
    }

    public GuiStack setSpacing(int spacing) {
        this.spacing = spacing;

        arrangeComponents();

        return this;
    }

    public GuiStack addComponent(DynamicGuiComponent component) {
        this.components.add(component);

        arrangeComponents();

        return this;
    }

    public GuiStack addComponent(DynamicGuiComponent... components) {
        this.components.addAll(Arrays.asList(components));

        arrangeComponents();

        return this;
    }

    public GuiStack addComponent(List<DynamicGuiComponent> components) {
        this.components.addAll(components);

        arrangeComponents();

        return this;
    }

    public GuiStack removeComponent(DynamicGuiComponent component) {
        this.components.remove(component);

        arrangeComponents();

        return this;
    }



    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(this.position.x, this.position.y, this.position.x + this.size.x, this.position.y + this.size.y, 0xFF000000, 0xFF000000);
    }

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }
}
