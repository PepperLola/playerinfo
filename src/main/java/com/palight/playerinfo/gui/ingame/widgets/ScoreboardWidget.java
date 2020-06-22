package com.palight.playerinfo.gui.ingame.widgets;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ScoreboardWidget extends Gui {

    private final Minecraft mc;
    private int headerColor = 1610612736;
    private int bodyColor = 1342177280;

    public ScoreboardWidget(Minecraft mc) {
        this.mc = mc;
    }

    public void render(ScoreObjective objective, ScaledResolution resolution) {
        if (ModConfiguration.scoreboardModEnabled && !ModConfiguration.scoreboardEnabled) return;
        headerColor = ModConfiguration.scoreboardHeaderColor;
        bodyColor = ModConfiguration.scoreboardBodyColor;
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> sortedScores = scoreboard.getSortedScores(objective);
        List<Score> filteredList = Lists.newArrayList(Iterables.filter(sortedScores, new Predicate<Score>() {
            public boolean apply(Score score) {
                return score.getPlayerName() != null && !score.getPlayerName().startsWith("#");
            }
        }));
        ArrayList list;
        if (filteredList.size() > 15) {
            list = Lists.newArrayList(Iterables.skip(filteredList, sortedScores.size() - 15));
        } else {
            list = (ArrayList) filteredList;
        }

        int stringWidth = mc.fontRendererObj.getStringWidth(objective.getDisplayName());

        String displayString;
        for (Iterator iterator = list.iterator(); iterator.hasNext(); stringWidth = Math.max(stringWidth, mc.fontRendererObj.getStringWidth(displayString))) {
            Score score = (Score) iterator.next();
            ScorePlayerTeam teamScore = scoreboard.getPlayersTeam(score.getPlayerName());
            displayString = ScorePlayerTeam.formatPlayerName(teamScore, score.getPlayerName());

            if (!ModConfiguration.scoreboardModEnabled || ModConfiguration.scoreboardNumbersEnabled) {
                displayString += ": " + EnumChatFormatting.RED + score.getScorePoints();
            }
        }

        int scoreboardHeight = list.size() * mc.fontRendererObj.FONT_HEIGHT;
        int lineHeight = resolution.getScaledHeight() / 2 + scoreboardHeight / 3;
        int padding = 3;
        int xPosition = resolution.getScaledWidth() - stringWidth - padding;
        int index = 0;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Score score = (Score) iterator.next();
            ++index;
            ScorePlayerTeam teamScore = scoreboard.getPlayersTeam(score.getPlayerName());
            String formattedPlayerName = ScorePlayerTeam.formatPlayerName(teamScore, score.getPlayerName());
            String scoreString = !ModConfiguration.scoreboardModEnabled || ModConfiguration.scoreboardNumbersEnabled ? EnumChatFormatting.RED + "" + score.getScorePoints() : "";
            int yPosition = lineHeight - index * mc.fontRendererObj.FONT_HEIGHT;
            int xEnd = resolution.getScaledWidth() - padding + 2;
            drawRect(xPosition - 2, yPosition, xEnd, yPosition + mc.fontRendererObj.FONT_HEIGHT, bodyColor);
            mc.fontRendererObj.drawString(formattedPlayerName, xPosition, yPosition, 553648127);
            mc.fontRendererObj.drawString(scoreString, xEnd - mc.fontRendererObj.getStringWidth(scoreString), yPosition, 553648127);
            if (index == list.size()) {
                String lvt_20_1_ = objective.getDisplayName();
                drawRect(xPosition - 2, yPosition - mc.fontRendererObj.FONT_HEIGHT - 1, xEnd, yPosition - 1, headerColor);
                drawRect(xPosition - 2, yPosition - 1, xEnd, yPosition, bodyColor);
                mc.fontRendererObj.drawString(lvt_20_1_, xPosition + stringWidth / 2 - mc.fontRendererObj.getStringWidth(lvt_20_1_) / 2, yPosition - mc.fontRendererObj.FONT_HEIGHT, 553648127);
            }
        }
    }

    public int getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(int headerColor) {
        this.headerColor = headerColor;
    }

    public int getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(int bodyColor) {
        this.bodyColor = bodyColor;
    }
}
