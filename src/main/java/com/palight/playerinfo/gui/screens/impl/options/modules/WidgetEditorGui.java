package com.palight.playerinfo.gui.screens.impl.options.modules;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.gui.ingame.widgets.impl.ScoreboardWidget;
import com.palight.playerinfo.gui.screens.CustomGuiScreen;
import com.palight.playerinfo.gui.widgets.impl.GuiWidgetOptions;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.modules.impl.gui.ScoreboardMod;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetEditorGui extends CustomGuiScreen {

    private final List<GuiIngameWidget> widgets = new ArrayList<>();

    private final Map<GuiIngameWidget, GuiWidgetOptions> optionsMap = new HashMap<>();

    private GuiIngameWidget editingWidget;

    private double moduleXDiff; // x difference between widget and mouse
    private double moduleYDiff; // y difference between widget and mouse

    private List<WidgetSnapLine> snapLines = null;
    private List<WidgetSnapLine> linesToDraw = new ArrayList<>();

    public WidgetEditorGui() {
        super("screen.widgetEditor");
    }

    @Override
    public void initGui() {
        int i = 0;
        for (Module module : PlayerInfo.getModules().values()) {
            GuiIngameWidget widget = module.getWidget();
            if (widget != null) {
                widgets.add(widget);
                this.optionsMap.put(widget, new GuiWidgetOptions(
                        widget,
                        i,
                        widget.getPosition().getX(),
                        widget.getPosition().getY() + widget.height + 2,
                        widget.getPosition().getX() + widget.width,
                        widget.getPosition().getY() + widget.height + 18
                ));
                i ++;
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

        this.optionsMap.forEach((widget, optionWidget) -> {
            optionWidget.mouseClicked(mouseX, mouseY);
        });

        editingWidget = getClickedWidget(mouseX, mouseY);
        if (editingWidget == null) return;
        if (!editingWidget.movable) return;

        editingWidget.setSelected(true);

        if (btn == 0) {

            this.snapLines = WidgetSnapLine.getLines(new ScaledResolution(Minecraft.getMinecraft()));

            int widgetX = editingWidget.getPosition().getX();
            int widgetY = editingWidget.getPosition().getY();

            if (editingWidget instanceof ScoreboardWidget) {
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
        if (btn == 0) {
            double posX = mouseX + moduleXDiff;
            double posY = mouseY + moduleYDiff;

            double newX = -1.0;
            double newY = -1.0;

            for (GuiIngameWidget widget : this.widgets) {
                if (newX != -1 && newY != -1) break;
                boolean shouldSnapTop = editingWidget.shouldSnapTop(widget, WidgetSnapLine.Axis.HORIZONTAL, mouseX + moduleXDiff, mouseY + moduleYDiff + editingWidget.height);
                boolean shouldSnapBottom = editingWidget.shouldSnapBottom(widget, WidgetSnapLine.Axis.HORIZONTAL, mouseX + moduleXDiff, mouseY + moduleYDiff);
                boolean shouldSnapLeft = editingWidget.shouldSnapTop(widget, WidgetSnapLine.Axis.VERTICAL, mouseX + moduleXDiff + editingWidget.width, mouseY + moduleYDiff);
                boolean shouldSnapRight = editingWidget.shouldSnapBottom(widget, WidgetSnapLine.Axis.VERTICAL, mouseX + moduleXDiff, mouseY + moduleYDiff);

//                boolean betweenVertically = Math.abs(editingWidget.getPosition().getY() - widget.getPosition().getY()) < widget.height;

                if (Math.abs(editingWidget.getPosition().getY() - widget.getPosition().getY()) < widget.height + editingWidget.height) {
                    if (shouldSnapLeft && mouseX + moduleXDiff + editingWidget.width < widget.getPosition().getX()) {
                        if (newX == -1) {
                            newX = widget.getPosition().getX() - editingWidget.width;
                        }
                    }

                    if (shouldSnapRight && mouseX + moduleXDiff >= widget.getPosition().getX() + widget.width) {
                        if (newX == -1) {
                            newX = widget.getPosition().getX() + widget.width;
                        }
                    }
                }

                if (Math.abs(editingWidget.getPosition().getX() - widget.getPosition().getX()) < widget.width + editingWidget.width) {
                    if (shouldSnapTop && editingWidget.getPosition().getY() - editingWidget.height <= widget.getPosition().getY()) {
                        if (newY == -1) {
                            newY = widget.getPosition().getY() - editingWidget.height;
                        }
                    }
                    if (shouldSnapBottom && editingWidget.getPosition().getY() >= widget.getPosition().getY() + widget.height) {
                        if (newY == -1) {
                            newY = widget.getPosition().getY() + widget.height;
                        }
                    }
                }
            }

            for (WidgetSnapLine line : snapLines) {
                if (newX != -1 && newY != -1) break;

                boolean shouldSnapMiddle = editingWidget.shouldSnapMiddle(line, mouseX + moduleXDiff, mouseY + moduleYDiff);
                boolean shouldSnapTop = editingWidget.shouldSnapTop(line, mouseX + moduleXDiff, mouseY + moduleYDiff);
                boolean shouldSnapBottom = editingWidget.shouldSnapBottom(line, mouseX + moduleXDiff, mouseY + moduleYDiff);
                if (shouldSnapMiddle || shouldSnapTop || shouldSnapBottom) {
                    linesToDraw.add(line);
                    if (shouldSnapMiddle) {
                        if (line.getAxis() == WidgetSnapLine.Axis.HORIZONTAL) {
                            if (newY == -1) {
                                newY = line.getPos() - editingWidget.height / 2.0;
                            }
                        } else {
                            if (newX == -1) {
                                newX = line.getPos() - editingWidget.width / 2.0;
                            }
                        }
                    }
                    if (shouldSnapTop) {
                        if (line.getAxis() == WidgetSnapLine.Axis.HORIZONTAL) {
                            if (newY == -1) {
                                newY = line.getPos();
                            }
                        } else {
                            if (newX == -1) {
                                newX = line.getPos();
                            }
                        }
                    }
                    if (shouldSnapBottom) {
                        if (line.getAxis() == WidgetSnapLine.Axis.HORIZONTAL) {
                            if (newY == -1) {
                                newY = line.getPos() - editingWidget.height;
                            }
                        } else {
                            if (newX == -1) {
                                newX = line.getPos() - editingWidget.width;
                            }
                        }
                    }
                } else {
                    linesToDraw.remove(line);
                }
            }

            if (newX != -1 || newY != -1) {
                if (newX == -1) newX = mouseX + moduleXDiff;
                if (newY == -1) newY = mouseY + moduleYDiff;
                editingWidget.getPosition().set(newX, newY);
                return;
            }

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
        if (editingWidget != null)
            editingWidget.setSelected(false);
        editingWidget = null;
        linesToDraw.clear();
        super.mouseReleased(mouseX, mouseY, btn);
    }

    private GuiIngameWidget getClickedWidget(int x, int y) {
        for (GuiIngameWidget widget : widgets) {
            if (widget == null) continue;
            double posX = widget.getPosition().getX_double();
            double posY = widget.getPosition().getY_double();

            if (widget instanceof ScoreboardWidget) {
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

        ScaledResolution res = new ScaledResolution(mc);
        this.linesToDraw.forEach(line -> this.renderLine(line, res));

        this.optionsMap.forEach((widget, optionWidget) -> {
            if (NumberUtil.pointIsBetween(
                    mouseX,
                    mouseY,
                    widget.getPosition().getX(),
                    optionWidget.isReversed() ?
                            widget.getPosition().getY() - 2 - optionWidget.height :
                            widget.getPosition().getY(),
                    widget.getPosition().getX() + widget.width,
                    optionWidget.isReversed() ?
                            widget.getPosition().getY() + widget.height :
                            widget.getPosition().getY() + widget.height + (optionWidget.isShouldShow() ? optionWidget.height : 0))) {

                optionWidget.setShouldShow(true);
            } else {
                optionWidget.setShouldShow(false);
            }

            if (optionWidget.isShouldShow()) {
                optionWidget.drawWidget(mc, mouseX, mouseY);
            }
        });
    }

    private void renderLine(WidgetSnapLine line, ScaledResolution res) {
        if (line.getAxis() == WidgetSnapLine.Axis.HORIZONTAL) {
            this.drawHorizontalLine(0, res.getScaledWidth(), line.getPos(), 0xffff0000);
        } else {
            this.drawVerticalLine(line.getPos(), res.getScaledHeight(), 0, 0xffff0000);
        }
        GlStateManager.color(1, 1, 1, 1);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
