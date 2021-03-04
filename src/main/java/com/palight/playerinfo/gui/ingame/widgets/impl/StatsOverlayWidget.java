package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.gui.StatsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class StatsOverlayWidget extends GuiIngameWidget {
    private StatsMod module;

    public StatsOverlayWidget() {
        super(-1, 4, 256, 256);

    }

    @Override
    public void render(Minecraft mc) {
        ScaledResolution res = new ScaledResolution(mc);
        if(this.getPosition().getX() == -1) {
            this.getPosition().setX(res.getScaledWidth()-this.width-4);
        }
        if(module == null){
            module = ((StatsMod) PlayerInfo.getModules().get("stats"));
        }
        super.render(mc);

        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

        int x = this.getPosition().getX();
        int y = this.getPosition().getY();

        int i = 0;
        for (StatsMod.PlayerStats playerStats : module.getPlayerStats().values()) {
            String playerName = playerStats.name;
            boolean nicked = playerStats.nicked;
            int level = playerStats.level;
            int karma = playerStats.karma;
            double wlr = playerStats.wlr;
            double kdr = playerStats.kdr;

            int rowY = y + (2 + fr.FONT_HEIGHT) * i;

            drawText(playerName,x+2,rowY);
            if (nicked){
                drawText("NICKED" ,x+52, rowY);
            } else {

                switch (playerStats.getGameType()) {
                    case BEDWARS:
                        break;
                }
            }
            i ++;
        }
    }
}
