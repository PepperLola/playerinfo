package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class DuelTrackerWidget extends GuiIngameWidget {
    public DuelTrackerWidget() {
        super(-1, -1, 64, -1);
    }

    private final List<String> lines = new ArrayList<>();
    private final List<String> linesQueue = new ArrayList<>();

    @Override
    public void render(Minecraft mc) {
        if (this.height == -1) {
            this.height = 4 * PlayerInfo.instance.fontRendererObj.FONT_HEIGHT + 6;
        }
        if (this.getState() == WidgetEditingState.EDITING) {
            super.render(mc);
            this.drawText("palight : 3.000", this.getPosition().getX(), this.getPosition().getY());
        } else if (this.getModule().isEnabled() && lines.size() > 0) {
            super.render(mc);

            lines.addAll(linesQueue);
            linesQueue.clear();

            while (lines.size() > 4) {
                lines.remove(0);
            }

            double maxWidth = 0;
            int offset = 0;
            for (String line : lines) {
                double width = PlayerInfo.instance.fontRendererObj.getWidth(line);
                if (width > maxWidth)
                    maxWidth = width;

                this.drawText(line, this.getPosition().getX() + 4, this.getPosition().getY() + offset);
                offset += PlayerInfo.instance.fontRendererObj.FONT_HEIGHT + 2;
            }
            this.width = (int) Math.floor(maxWidth + 8);
        }
    }

    public void setLastHit(EntityPlayer player, double distance) {
        linesQueue.add(player.getName() + " : " + Math.round(distance * 1000) / 1000.0);
    }
}
