package com.palight.playerinfo.gui.widgets.impl;

import com.palight.playerinfo.gui.widgets.GuiCustomWidget;
import com.palight.playerinfo.modules.impl.misc.TextReplacementMod;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class GuiEmojiPicker extends GuiCustomWidget {
    private int columns;
    private int rows;
    public int offset = 0;
    private int cellWidth;
    private int cellHeight;

    public GuiEmojiPicker(int id, int xPosition, int yPosition, int width, int height, int columns, int rows, int offset) {
        super(id, xPosition, yPosition, width, height);

        this.columns = columns;
        this.rows = rows;
        this.offset = 0;

        this.cellWidth = width / columns;
        this.cellHeight = height / rows;
    }

    public GuiEmojiPicker(int id, int xPosition, int yPosition, int width, int height, int columns, int rows) {
        this(id, xPosition, yPosition, width, height, columns, rows, 0);
    }

    @Override
    public void drawWidget(Minecraft mc, int mouseX, int mouseY) {
        super.drawWidget(mc, mouseX, mouseY);

        drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0x99000000, 0x99000000);

        FontRenderer fr = mc.fontRendererObj;

        for (int i = 0; i < Math.min(TextReplacementMod.emojiList.size() - offset, columns * rows); i++) {
            if (i - offset >= columns * rows) break;
            String emoji = TextReplacementMod.emojiList.get(i + offset);
            int x = i % columns;
            int y = i / rows;

            int cellX = this.xPosition + x * cellWidth;
            int cellY = this.yPosition + y * cellHeight;

            if (NumberUtil.pointIsBetween(mouseX, mouseY, cellX, cellY, cellX + cellWidth, cellY + cellHeight)) {
                drawGradientRect(cellX, cellY, cellX + cellWidth, cellY + cellHeight, 0x66ffffff, 0x66ffffff);
            }

            fr.drawString(emoji,
                    cellX + (cellWidth - fr.getStringWidth(emoji)) / 2,
                    cellY + (cellHeight - fr.FONT_HEIGHT) / 2,
                    0xffffffff
            );
        }
    }

    public String widgetClicked(int mouseX, int mouseY) {
        super.mouseClicked(mouseX, mouseY);

        if (!NumberUtil.pointIsBetween(mouseX, mouseY, this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height)) return "";

        int x = (mouseX - this.xPosition) / this.cellWidth;
        int y = (mouseY - this.yPosition) / this.cellHeight;

        int i = offset + (y * rows) + x;
        if (i >= TextReplacementMod.nameList.size()) return "";

        return TextReplacementMod.nameList.get(i);
    }

    public void handleScrolling(int x, int y, int i) {
        if (i != 0) {
            if (NumberUtil.pointIsBetween(
                                x,
                                y,
                                this.xPosition,
                                this.yPosition,
                                this.xPosition + this.width,
                                this.yPosition + this.height
                        )) {
                i = -1 * (i > 0 ? 1 : -1);

                i *= columns * rows;

                if (this.offset + i >= 0 && this.offset + i < TextReplacementMod.emojiList.size()) {
                    this.offset += i;
                }
            }
        }
    }
}
