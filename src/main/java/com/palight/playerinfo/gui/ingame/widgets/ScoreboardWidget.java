package com.palight.playerinfo.gui.ingame.widgets;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.palight.playerinfo.options.ModConfiguration;
import net.minecraft.client.Minecraft;
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

public class ScoreboardWidget extends GuiIngameWidget {

    private final Minecraft mc;
    private int defaultX;
    private int defaultY;
    private int headerColor = 1610612736;
    private int bodyColor = 1342177280;

    public ScoreboardWidget(Minecraft mc) {
        super(-1, -1, 100, 100);
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

        this.height = list.size() * mc.fontRendererObj.FONT_HEIGHT;
        int defaultY = resolution.getScaledHeight() / 2 + this.height / 3;
        this.yPosition = yPosition == -1 ? defaultY : yPosition;

        int padding = 3;
        int defaultX = resolution.getScaledWidth() - stringWidth - padding;

        this.xPosition = xPosition == -1 ? defaultX : xPosition;
        this.width = stringWidth + 2;

        int xEnd = this.xPosition + this.width;

        for (int i = 0; i < list.size(); i++) {
            Score score = (Score) list.get(i);

            ScorePlayerTeam teamScore = scoreboard.getPlayersTeam(score.getPlayerName());

            String formattedPlayerName = ScorePlayerTeam.formatPlayerName(teamScore, score.getPlayerName());
            String scoreString = !ModConfiguration.scoreboardModEnabled || ModConfiguration.scoreboardNumbersEnabled ? EnumChatFormatting.RED.toString() + score.getScorePoints() : "";

            int lineY = yPosition + (i * mc.fontRendererObj.FONT_HEIGHT);

            drawRect(xPosition - 2, lineY, xEnd, lineY + mc.fontRendererObj.FONT_HEIGHT, bodyColor);

            drawText(formattedPlayerName, xPosition, lineY);
            drawText(scoreString, xEnd - mc.fontRendererObj.getStringWidth(scoreString), lineY);
        }

        String objectiveDisplayName = objective.getDisplayName();

        drawRect(xPosition - 2, yPosition - mc.fontRendererObj.FONT_HEIGHT, xEnd, yPosition, headerColor);

        drawText(objectiveDisplayName, xPosition + stringWidth / 2 - mc.fontRendererObj.getStringWidth(objectiveDisplayName) / 2, this.yPosition - mc.fontRendererObj.FONT_HEIGHT);
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
