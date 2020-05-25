package com.palight.playerinfo.gui.widgets;

import net.minecraft.client.gui.Gui;

public class GuiCheckBox extends Gui {
    public int height;
    public int width;
    public int xPosition;
    public int yPosition;
    public int id;
    public boolean checked = false;

    public GuiCheckBox(int id, int x, int y, boolean checked) {
        this(id, x, y, 16, 16, checked);
    }

    public GuiCheckBox(int id, int x, int y, int width, int height, boolean checked) {
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.checked = checked;
    }
}
