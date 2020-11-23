package com.palight.playerinfo.gui.ingame.widgets;

public class WidgetPosition {
    private GuiIngameWidget widget;
    private int x;
    private int y;

    public WidgetPosition(GuiIngameWidget widget, int x, int y) {
        this.widget = widget;
        this.x = x;
        this.y = y;
    }

    public WidgetPosition(GuiIngameWidget widget) {
        this.widget = widget;
        this.x = 0;
        this.y = 0;
    }

    public GuiIngameWidget getModule() {
        return widget;
    }

    public void setModule(GuiIngameWidget widget) {
        this.widget = widget;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
