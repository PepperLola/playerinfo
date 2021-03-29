package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.gui.StatsMod;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;


public class StatsOverlayWidget extends GuiIngameWidget {
    private StatsMod module;

    public StatsOverlayWidget() {
        super(-1, 4, 256, 256);

    }

    @Override
    public void render(Minecraft mc) {
        ScaledResolution res = new ScaledResolution(mc);
        if (this.getPosition().getX() == -1) {
            this.getPosition().setX(res.getScaledWidth() - this.width - 4);
        }
        if (module == null) {
            module = ((StatsMod) PlayerInfo.getModules().get("stats"));
        }

        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

        int x = this.getPosition().getX();
        int y = this.getPosition().getY();

        int offset = 30;
        int padding = 2;
        UUID clientUUID = Minecraft.getMinecraft().thePlayer.getUniqueID();
        if (!module.getPlayerStats().containsKey(clientUUID) || module.getPlayerStats().size() <= 0) return;
        super.render(mc);

        drawText("NAME", x + padding, y + padding);
        drawText("TAG", x + padding + 100, y + padding);
        drawText("LEVEL", x + padding + 100 + offset - 10, y + padding);
        drawText("WLR", x + padding + 100 + offset * 2, y + padding);
        drawText("KDR", x + padding + 100 + offset * 3, y + padding);
        StatsMod.PlayerStats userStats = module.getPlayerStats().get(clientUUID);
        switch (userStats.getGameType()) {
            case BEDWARS:
                drawText("STARS", x + padding + 100 + offset * 4, y + padding);
                drawText("FKDR", x + padding + 100 + offset * 5, y + padding);
                drawText("BBLR", x + padding + 100 + offset * 6, y + padding);
                break;
            case DUELS:
                drawText("TITLE", x + padding + 100 + offset * 4, y + padding);
        }


        int i = 0;
        for (StatsMod.PlayerStats playerStats : module.getPlayerStats().values()) {
            String playerName = playerStats.name;
            boolean nicked = playerStats.nicked;
            int level = playerStats.level;
            double wlr = NumberUtil.round(playerStats.wlr, 2);
            double kdr = NumberUtil.round(playerStats.kdr, 2);
            int gameLevel = playerStats.gameLevel;

            int rowY = y + (2 + fr.FONT_HEIGHT) * (i + 1);

            drawText(playerName, x + padding, rowY);
            if (nicked) {
                drawText("NICKED", x + padding + 90, rowY);
            } else {

                drawText(String.valueOf(level) + "✫", x + padding + 100 + offset - 10, rowY);
                drawText(String.valueOf(wlr), x + padding + 100 + offset * 2, rowY);
                drawText(String.valueOf(kdr), x + padding + 100 + offset * 3, rowY);

                StatsMod.GameType gameType = playerStats.getGameType();
                if (gameType == null) continue;
                switch (playerStats.getGameType()) {
                    case BEDWARS:

                        double fkdr = NumberUtil.round(playerStats.fkdr, 2);
                        double bblr = NumberUtil.round(playerStats.bblr, 2);
                        drawText(String.valueOf(gameLevel), x + padding + 100 + offset * 4, rowY);
                        drawText(String.valueOf(fkdr), x + padding + 100 + offset * 5, rowY);
                        drawText(String.valueOf(bblr), x + padding + 100 + offset * 6, rowY);
                        break;
                    case DUELS:
                        String title = playerStats.title;
                        int prestige = playerStats.prestige;
                        drawText(StringUtils.capitalize(title) + " " + prestige, x + padding + 100 + offset * 4, rowY);
                        break;
                }
            }
            i++;
        }
    }
}
