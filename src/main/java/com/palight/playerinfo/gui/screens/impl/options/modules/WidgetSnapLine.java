package com.palight.playerinfo.gui.screens.impl.options.modules;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntSupplier;

public class WidgetSnapLine {
    private IntSupplier pos, threshold;
    private Axis axis;

    public static List<WidgetSnapLine> lines = new ArrayList<>();

    public static List<WidgetSnapLine> getLines(ScaledResolution res) {
        return Arrays.asList(
                new WidgetSnapLine(0, Axis.HORIZONTAL, SnapStrength.MEDIUM),
                new WidgetSnapLine(0, Axis.VERTICAL, SnapStrength.MEDIUM),
                new WidgetSnapLine(10, Axis.HORIZONTAL, SnapStrength.MEDIUM),
                new WidgetSnapLine(10, Axis.VERTICAL, SnapStrength.MEDIUM),
                new WidgetSnapLine(res.getScaledHeight(), Axis.HORIZONTAL, SnapStrength.MEDIUM),
                new WidgetSnapLine(res.getScaledWidth(), Axis.VERTICAL, SnapStrength.MEDIUM),
                new WidgetSnapLine(res.getScaledHeight() - 10, Axis.HORIZONTAL, SnapStrength.MEDIUM),
                new WidgetSnapLine(res.getScaledWidth() - 10, Axis.VERTICAL, SnapStrength.MEDIUM),
                new WidgetSnapLine(res.getScaledHeight() / 2, Axis.HORIZONTAL, SnapStrength.MEDIUM),
                new WidgetSnapLine(res.getScaledWidth() / 2, Axis.VERTICAL, SnapStrength.MEDIUM)
        );
    }

    /**
     * Creates a new line for a widget to snap to.
     *
     * @param pos  Position along the axis provided (x value if vertical, y if horizontal) from top left
     * @param axis Whether it's a vertical or horizontal line
     */
    public WidgetSnapLine(int pos, Axis axis) {
        this(pos, axis, 10);
    }

    public WidgetSnapLine(int pos, Axis axis, int threshold) {
        this(() -> pos, axis, () -> threshold);
    }

    /**
     * Creates a new line for a widget to snap to.
     *
     * @param pos      Position along the axis provided (x value if vertical, y if horizontal) from top left
     * @param axis     Whether it's a vertical or horizontal line
     * @param strength Strength of the snap (enum with distance threshold for snapping)
     */
    public WidgetSnapLine(int pos, Axis axis, SnapStrength strength) {
        this(() -> pos, axis, strength::getThreshold);
    }

    /**
     * Creates a new line for a widget edge to snap to.
     *
     * @param pos       Position along the axis provided (x value if vertical, y if horizontal) from top left
     * @param axis      Whether it's a vertical or horizontal line
     * @param threshold How close the widget has to be to snap to the line
     */
    public WidgetSnapLine(IntSupplier pos, Axis axis, IntSupplier threshold) {
        this.pos = pos;
        this.axis = axis;
        this.threshold = threshold;
    }

    public void render(Gui gui, ScaledResolution res) {
        if (axis == Axis.HORIZONTAL) {
            Gui.drawRect(0, pos.getAsInt(), res.getScaledWidth(), pos.getAsInt() + 1, 0xffffffff);
        } else {
            Gui.drawRect(pos.getAsInt(), 0, pos.getAsInt() + 1, res.getScaledHeight(), 0xffffffff);
        }
    }

    public void setPos(int pos) {
        this.setPos(() -> pos);
    }

    public void setPos(IntSupplier pos) {
        this.pos = pos;
    }

    public void setThreshold(int threshold) {
        this.setThreshold(() -> threshold);
    }

    public void setThreshold(IntSupplier threshold) {
        this.threshold = threshold;
    }

    public void setAxis(Axis axis) {
        this.axis = axis;
    }

    public int getPos() {
        return pos.getAsInt();
    }

    public IntSupplier getPosSupplier() {
        return this.pos;
    }

    public int getThreshold() {
        return threshold.getAsInt();
    }

    public IntSupplier getThresholdSupplier() {
        return this.threshold;
    }

    public Axis getAxis() {
        return axis;
    }

    public enum Axis {
        HORIZONTAL,
        VERTICAL
    }

    public enum SnapStrength {
        WEAK(5),
        MEDIUM(10),
        STRONG(15);

        private int threshold;

        SnapStrength(int threshold) {
            this.threshold = threshold;
        }

        public int getThreshold() {
            return threshold;
        }
    }
}
