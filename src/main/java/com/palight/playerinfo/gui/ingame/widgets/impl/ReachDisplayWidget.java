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

        String displayString = "";

        if (getState() == WidgetEditingState.INGAME && System.currentTimeMillis() - ReachDisplayMod.lastAttackTime < ReachDisplayMod.displayTime) {
            displayString = NumberUtil.round(ReachDisplayMod.reach, 2) + " blocks";
        } else if (getState() == WidgetEditingState.EDITING) {
            displayString = "3 blocks";
        }

        if (!displayString.equals("")) {
            this.width = (int) (PlayerInfo.instance.fontRendererObj.getWidth(displayString)) + 4;
            this.height = (int) (PlayerInfo.instance.fontRendererObj.getHeight(displayString) + 2);

            super.render(mc);
            drawTextVerticallyCentered(displayString, getPosition().getX() + 2, getPosition().getY() + this.height / 2 + 1);
        }
    }

}