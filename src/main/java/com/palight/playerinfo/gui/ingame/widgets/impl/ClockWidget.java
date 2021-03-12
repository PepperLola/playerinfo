package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.gui.ClockMod;
import net.minecraft.client.Minecraft;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockWidget extends GuiIngameWidget {

    private ClockMod module;

    public ClockWidget() {
        super(-1, -1, 32, 16);
    }

    @Override
    public void render(Minecraft mc) {
        if (module == null) {
            module = ((ClockMod) PlayerInfo.getModules().get("clock"));
        }
        String time = new SimpleDateFormat(module.dateFormat != null ? module.dateFormat : "hh:mm:ss").format(new Date());
        int width = (int) Math.floor(PlayerInfo.instance.fontRendererObj.getWidth(time));
        if (width + 4 > this.width || Math.abs(this.width - width) > 16)
            this.width = width + 4;
        super.render(mc);
        this.drawText(time, this.getPosition().getX() + 2, this.getPosition().getY() + 2);
    }
}
