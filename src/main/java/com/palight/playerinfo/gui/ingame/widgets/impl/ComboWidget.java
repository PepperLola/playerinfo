package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.misc.ComboMod;
import net.minecraft.client.Minecraft;

public class ComboWidget extends GuiIngameWidget {

    private ComboMod module;

    public ComboWidget() {
        super(-1, -1, 32, 12);
    }

    @Override
    public void render(Minecraft mc) {

        if (module == null) {
            module = ((ComboMod) PlayerInfo.getModules().get("combo"));
        }

        super.render(mc);
        String text = this.module.combo + " hits";
        this.drawTextVerticallyCentered(this.module.combo + " hits", this.getPosition().getX() + ((int) Math.floor((this.width - PlayerInfo.instance.fontRendererObj.getWidth(text)))) / 2, this.getPosition().getY() + this.height / 2);
    }
}
