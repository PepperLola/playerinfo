package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.gui.ReachDisplayMod;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ReachDisplayWidget extends GuiIngameWidget {


    public ReachDisplayWidget() {
        super(-1, 64, 16, 11);
    }


    @Override
    public void render(Minecraft mc) {

        if (this.getPosition().getX() == -1) {
            ScaledResolution res = new ScaledResolution(mc);
            this.getPosition().setX(res.getScaledWidth() / 2 - this.width / 2);
        }

        if (getState() == WidgetEditingState.EDITING) {

            super.render(mc);

            String displayString = "3 blocks";

            this.width = (int) (PlayerInfo.instance.fontRendererObj.getWidth(displayString) + 4);
            this.height = (int) (PlayerInfo.instance.fontRendererObj.getHeight(displayString) + 2);

            drawText(displayString, getPosition().getX() + 2, getPosition().getY() + 1);

        } else if (System.currentTimeMillis() - ReachDisplayMod.lastAttackTime < ReachDisplayMod.displayTime) {

            super.render(mc);

            String displayString = NumberUtil.round(ReachDisplayMod.reach, 2) + " blocks";

            this.width = (int) (PlayerInfo.instance.fontRendererObj.getWidth(displayString) + 4);
            this.height = (int) (PlayerInfo.instance.fontRendererObj.getHeight(displayString) + 2);

            drawText(displayString, getPosition().getX() + 2, getPosition().getY() + 1);
        }

    }

}