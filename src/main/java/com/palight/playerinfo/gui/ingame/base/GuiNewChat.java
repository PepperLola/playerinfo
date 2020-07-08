package com.palight.playerinfo.gui.ingame.base;

import net.minecraft.client.Minecraft;

public class GuiNewChat extends net.minecraft.client.gui.GuiNewChat {
    public GuiNewChat(Minecraft mc) {
        super(mc);
    }

    @Override
    public void addToSentMessages(String msg) {
        super.addToSentMessages(msg);
        System.out.println(msg);
    }
}
