package com.palight.playerinfo.gui.screens.impl.options.modules;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.gui.ingame.widgets.impl.ScoreboardWidget;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.impl.gui.ScoreboardMod;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WidgetEditorGui extends CustomGuiScreen {

    private final List<GuiIngameWidget> widgets = new ArrayList<>();

    private GuiIngameWidget editingWidget;

    private double moduleXDiff; // x difference between widget and mouse
    private double moduleYDiff; // y difference between widget and mouse

    public WidgetEditorGui() {
        super("screen.widgetEditor");
    }

    @Override
    public void initGui() {
        for (Module module : PlayerInfo.getModules().values()) {
            GuiIngameWidget widget = module.getWidget();
            if (widget != null) {
                widgets.add(widget);
            }
            module.startEditingWidgets();
        }
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (Module module : PlayerInfo.getModules().values()) {
            widgets.remove(module.getWidget());
            module.stopEditingWidgets();
        }
        PlayerInfo.saveWidgetPositions();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
        editingWidget = getClickedWidget(mouseX, mouseY);
        if (editingWidget == null) return;
        if (!editingWidget.movable) return;

        if (btn == 0) {

            int widgetX = editingWidget.getPosition().getX();
            int widgetY = editingWidget.getPosition().getY();

            if (editingWidget instanceof ScoreboardWidget) {
                ScoreboardWidget scoreboardWidget = (ScoreboardWidget) editingWidget;
                ScoreboardMod scoreboardMod = (ScoreboardMod) PlayerInfo.getModules().get("scoreboard");
                widgetX += scoreboardMod.offsetX;
                widgetY += scoreboardMod.offsetY;
            }

            moduleXDiff = widgetX - mouseX;
            moduleYDiff = widgetY - mouseY;
        } else if (btn == 1) {
            editingWidget.toggleChroma();
        }
        super.mouseClicked(mouseX, mouseY, btn);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int btn, long p_mouseClickMove_4_) {
        if (editingWidget == null) return;
        if (!editingWidget.movable) return;
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        if (btn == 0) {
            double posX = mouseX + moduleXDiff;
            double posY = mouseY + moduleYDiff;

            if (editingWidget instanceof ScoreboardWidget) {
                ScoreboardWidget scoreboardWidget = (ScoreboardWidget) editingWidget;
                ScoreboardMod scoreboardMod = (ScoreboardMod) PlayerInfo.getModules().get("scoreboard");

                scoreboardMod.offsetX = (int) posX - scoreboardWidget.getPosition().getX();
                scoreboardMod.offsetY = (int) posY - scoreboardWidget.getPosition().getY();
            } else {
                editingWidget.getPosition().setX(posX);
                editingWidget.getPosition().setY(posY);
            }
        }
        super.mouseClickMove(mouseX, mouseY, btn, p_mouseClickMove_4_);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int btn) {
        editingWidget = null;
        super.mouseReleased(mouseX, mouseY, btn);
    }

    private GuiIngameWidget getClickedWidget(int x, int y) {
        for (GuiIngameWidget widget : widgets) {
            if (widget == null) continue;
            double posX = widget.getPosition().getX_double();
            double posY = widget.getPosition().getY_double();

            if (widget instanceof ScoreboardWidget) {
                ScoreboardWidget scoreboardWidget = (ScoreboardWidget) editingWidget;
                ScoreboardMod scoreboardMod = (ScoreboardMod) PlayerInfo.getModules().get("scoreboard");

                posX += scoreboardMod.offsetX;
                posY += scoreboardMod.offsetY;
            }

            if (NumberUtil.pointIsBetween(x, y, posX, posY, posX + widget.width, posY + widget.height)) {
                return widget;
            }
        }

        return null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
