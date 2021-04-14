package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.Module;
import org.apache.commons.lang3.StringUtils;

public class WidgetState {
    private GuiIngameWidget widget;
    private double x;
    private double y;
    private boolean chroma = false;

    public WidgetState(GuiIngameWidget widget, int x, int y, boolean chroma) {
        this.widget = widget;
        this.x = x;
        this.y = y;
        this.chroma = chroma;
    }

    public WidgetState(GuiIngameWidget widget, double x, double y, boolean chroma) {
        this.widget = widget;
        this.x = x;
        this.y = y;
        this.chroma = chroma;
    }

    public WidgetState(GuiIngameWidget widget) {
        this.widget = widget;
        this.x = 0;
        this.y = 0;
        this.chroma = false;
    }

    public GuiIngameWidget getModule() {
        return widget;
    }

    public void setModule(GuiIngameWidget widget) {
        this.widget = widget;
    }

    public int getX() {
        return (int) x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(double x) { this.x = x; }

    public void setY(double y) { this.y = y; }

    public double getX_double() { return x; }

    public double getY_double() { return y; }

    public GuiIngameWidget getWidget() {
        return widget;
    }

    public void setWidget(GuiIngameWidget widget) {
        this.widget = widget;
    }

    public boolean isChroma() {
        return chroma;
    }

    public void setChroma(boolean chroma) {
        this.chroma = chroma;
    }

    public static WidgetState fromString(String in) {
        String data = StringUtils.substringBetween(in, "(", ")");
        String[] split = data.split(",");
        String moduleId = split[0];
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        boolean chroma = Boolean.parseBoolean(split[3]);

        Module module = PlayerInfo.getModules().get(moduleId);
        if (module == null) {
            return null;
        }

        return new WidgetState(PlayerInfo.getModules().get(moduleId).getWidget(), x, y, chroma);
    }

    @Override
    public String toString() {
        return String.format("WidgetState(%s,%f,%f,%b)", getModule().getModule().getId(), this.x, this.y, this.chroma);
    }
}
