package com.palight.playerinfo.gui.dynamic.impl;

import com.palight.playerinfo.gui.dynamic.DynamicGuiScreen;
import com.palight.playerinfo.gui.dynamic.components.GuiButton;
import com.palight.playerinfo.gui.dynamic.components.GuiStack;
import com.palight.playerinfo.util.math.Vector2;

public class TestDynamicGuiScreen extends DynamicGuiScreen {
    public TestDynamicGuiScreen() {
        super("screen.test", new Vector2<>(512, 472));
    }

    @Override
    public void setup() {
        GuiStack stack = this.createStack(0, 0, 4);

        stack.addComponent(this.createLabel("Hello World!", 0, 0));

        stack.addComponent(this.createSpacer(0, 0, 10, 10));

        stack.addComponent(this.createButton("Test Button", 0, 0, 100, 20).<GuiButton>setOnClick((mousePos, mouseButton) -> {
            System.out.println("Test Button Clicked with mouse button " + mouseButton);
        }));

        stack.addComponent(this.createCheckbox("Test Checkbox", 0, 0, 20, 20, false));

        stack.addComponent(this.createDropdown(new String[]{"Option 1", "Option 2", "Option 3", "Option 4", "Option 5"}, 0, 0, 256, 20));
    }
}
