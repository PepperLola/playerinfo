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

        int offset = 30;

        drawText("NAME", x+2,y+2);
        drawText("TAG", x+2+100,y+2);
        drawText("LEVEL", x+2+100+offset-10,y+2);
        drawText("WLR", x+2+100+offset*2,y+2);
        drawText("KDR", x+2+100+offset*3,y+2);



        int i = 0;
        for (StatsMod.PlayerStats playerStats : module.getPlayerStats().values()) {
            String playerName = playerStats.name;
            boolean nicked = playerStats.nicked;
            int level = playerStats.level;
            int karma = playerStats.karma;
            double wlr = playerStats.wlr;
            double kdr = playerStats.kdr;

            int rowY = y + (2 + fr.FONT_HEIGHT) * (i+1);

            drawText(playerName,x+2,rowY);
            if (nicked){
                drawText("NICKED" ,x+2+100, rowY);
            } else {

                drawText(String.valueOf(level), x+2+100+offset-10,rowY);
                drawText(String.valueOf(wlr), x+2+100+offset*2,rowY);
                drawText(String.valueOf(kdr), x+2+100+offset*3,rowY);

                StatsMod.GameType gameType = playerStats.getGameType();
                if(gameType == null) continue;
                switch (playerStats.getGameType()) {
                    case BEDWARS:
                        break;
                }
            }
            i ++;
        }
    }
}
