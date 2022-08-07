package com.palight.playerinfo.gui.dynamic.components;

import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.util.math.Vector2;

public class GuiSpacer extends DynamicGuiComponent {
    public GuiSpacer(DynamicGuiScreen screen, Vector2<Integer> position, Vector2<Integer> size) {
        super(screen, position, size);
    }

    public GuiSpacer(DynamicGuiScreen screen, int x, int y, int width, int height) {
        this(screen, new Vector2<>(x, y), new Vector2<>(width, height));
    }

    public GuiSpacer(DynamicGuiScreen screen, int x, int y) {
        this(screen, x, y, 10, 10);
    }

    public GuiSpacer(DynamicGuiScreen screen) {
        this(screen, 0, 0);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        // NO-OP
    }
}
