package com.palight.playerinfo.gui.ingame.widgets;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.Module;
import org.apache.commons.lang3.StringUtils;

public class WidgetState {
    private GuiIngameWidget widget;
    private int x;
    private int y;
    private boolean chroma = false;

    public WidgetState(GuiIngameWidget widget, int x, int y, boolean chroma) {
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
        int x = Integer.parseInt(split[1]);
        int y = Integer.parseInt(split[2]);
        boolean chroma = Boolean.parseBoolean(split[3]);

        Module module = PlayerInfo.getModules().get(moduleId);
        if (module == null) {
            return null;
        }

        return new WidgetState(PlayerInfo.getModules().get(moduleId).getWidget(), x, y, chroma);
    }

    @Override
    public String toString() {
        return String.format("WidgetState(%s,%d,%d,%b)", getModule().getModule().getId(), this.x, this.y, this.chroma);
    }
}
