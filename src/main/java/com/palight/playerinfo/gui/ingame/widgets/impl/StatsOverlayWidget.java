package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.gui.StatsMod;
import com.palight.playerinfo.util.HypixelUtil;
import com.palight.playerinfo.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;


public class StatsOverlayWidget extends GuiIngameWidget {
    private StatsMod module;

    private static final String[] ROMAN_NUMERALS = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    public StatsOverlayWidget() {
        super(-1, 4, 288, 256);
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

        int offset = 30; //distance between the stats
        int padding = 2; //padding
        UUID clientUUID = Minecraft.getMinecraft().thePlayer.getUniqueID();
        if (!StatsMod.getPlayerStats().containsKey(clientUUID) || StatsMod.getPlayerStats().size() <= 0) return;
        super.render(mc);

        drawText("NAME", x + padding, y + padding);
        drawText("TAG", x + padding + 125, y + padding);
        drawText("LEVEL", x + padding + 125 + offset - 10, y + padding);
        drawText("WLR", x + padding + 125 + offset * 2, y + padding);
        drawText("KDR", x + padding + 125 + offset * 3, y + padding);
        StatsMod.PlayerStats userStats = StatsMod.getPlayerStats().get(clientUUID);
        switch (userStats.getGameType()) {
            case BEDWARS:
                drawText("STARS", x + padding + 125 + offset * 4, y + padding);
                drawText("FKDR", x + padding + 125 + offset * 5, y + padding);
                drawText("BBLR", x + padding + 125 + offset * 6, y + padding);
                break;
            case DUELS:
                drawText("TITLE", x + padding + 125 + offset * 4, y + padding);
        }


        int i = 0;
        for (StatsMod.PlayerStats playerStats : StatsMod.getPlayerStats().values()) {
            String playerName = playerStats.name;
            boolean nicked = playerStats.nicked;
            int level = playerStats.level;
            double wlr = NumberUtil.round(playerStats.wlr, 2);
            double kdr = NumberUtil.round(playerStats.kdr, 2);
            int gameLevel = playerStats.gameLevel;
            HypixelUtil.Rank rank = playerStats.rank;


            int rowY = y + (2 + fr.FONT_HEIGHT) * (i + 1);


            if(rank == HypixelUtil.Rank.NONE) {
                drawText(playerName, x + padding, rowY, rank.getColor());
            } else {
                String rankTextBeforePlus = "[" + rank.getDisplayName().replaceAll("[+]", "");
                drawText(rankTextBeforePlus, x + padding, rowY, rank.getColor());
                int textWidth = (int) Math.floor(PlayerInfo.instance.fontRendererObj.getWidth(rankTextBeforePlus));
                String plusText = "+" + (rank == HypixelUtil.Rank.SUPERSTAR ? "+" : "");
                if (rank == HypixelUtil.Rank.MVP_PLUS || rank == HypixelUtil.Rank.SUPERSTAR) {
                    drawText(plusText, x + padding + textWidth, rowY, playerStats.plusColor.getColor());
                } else if (rank == HypixelUtil.Rank.VIP_PLUS) {
                    drawText("+", x + padding + textWidth, rowY, HypixelUtil.PlusColor.GOLD.getColor());
                }
                if (rank != HypixelUtil.Rank.VIP && rank != HypixelUtil.Rank.MVP) {
                    textWidth += (int) Math.floor(PlayerInfo.instance.fontRendererObj.getWidth(plusText));
                }
                drawText("] " + playerName, x + padding + textWidth, rowY, rank.getColor());
            }

            if (nicked) {
                drawText("NICKED", x + padding + 90, rowY, 11141120);
            } else {

                drawText(String.valueOf(level), x + padding + 125 + offset - 10, rowY);
                drawText(String.valueOf(wlr), x + padding + 125 + offset * 2, rowY);
                drawText(String.valueOf(kdr), x + padding + 125 + offset * 3, rowY);
                StatsMod.GameType gameType = playerStats.getGameType();
                if (gameType == null) continue;
                switch (playerStats.getGameType()) {
                    case BEDWARS:
                        double fkdr = NumberUtil.round(playerStats.fkdr, 2);
                        double bblr = NumberUtil.round(playerStats.bblr, 2);
                        drawText(String.valueOf(gameLevel) + "✫", x + padding + 125 + offset * 4, rowY);
                        drawText(String.valueOf(fkdr), x + padding + 125 + offset * 5, rowY);
                        drawText(String.valueOf(bblr), x + padding + 125 + offset * 6, rowY);
                        break;
                    case DUELS:
                        String title = playerStats.title;
                        int prestige = playerStats.prestige;
                        drawText(StringUtils.capitalize(title) + " " + ROMAN_NUMERALS[prestige], x + padding + 125 + offset * 4, rowY, HypixelUtil.TitleColor.getTitleColorFromName(title).getTitleColor());
                        break;
                }
            }
            i++;
        }
    }
}
