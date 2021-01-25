package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.palight.playerinfo.events.ScoreboardTitleChangeEvent;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.options.ModConfiguration;
import com.palight.playerinfo.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;

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
    public ScoreObjective scoreObjective;

    public ScoreboardWidget(Minecraft mc) {
        super(-1, -1, 100, 100);
        this.mc = mc;
        this.movable = false;
    }

    public void render(ScoreObjective objective, ScaledResolution resolution) {
        if (ModConfiguration.scoreboardModEnabled && !ModConfiguration.scoreboardEnabled) return;

        if (scoreObjective != null && objective != null) {
            if (!ColorUtil.stripColor(scoreObjective.getDisplayName()).equals(ColorUtil.stripColor(objective.getDisplayName()))) {
                MinecraftForge.EVENT_BUS.post(new ScoreboardTitleChangeEvent(scoreObjective.getDisplayName(), objective.getDisplayName()));
            }
        }
        scoreObjective = objective;

        headerColor = ModConfiguration.scoreboardHeaderColor;
        bodyColor = ModConfiguration.scoreboardBodyColor;

        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> sortedScores = scoreboard.getSortedScores(objective);

        List<Score> filteredList = Lists.newArrayList(Iterables.filter(sortedScores, score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#")));

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
        getPosition().setY(resolution.getScaledHeight() / 2 + this.height / 3);

        int padding = 3;
        getPosition().setX(resolution.getScaledWidth() - stringWidth - padding);

        this.width = stringWidth + 2;

        int xEnd = getPosition().getX() + this.width;

        for (int i = 0; i < list.size(); i++) {
            Score score = (Score) list.get(i);

            ScorePlayerTeam teamScore = scoreboard.getPlayersTeam(score.getPlayerName());

            String formattedPlayerName = ScorePlayerTeam.formatPlayerName(teamScore, score.getPlayerName());
            String scoreString = !ModConfiguration.scoreboardModEnabled || ModConfiguration.scoreboardNumbersEnabled ? EnumChatFormatting.RED.toString() + score.getScorePoints() : "";

            int lineY = getPosition().getY() - (i * mc.fontRendererObj.FONT_HEIGHT);

            drawRect(getPosition().getX() - 2, lineY, xEnd, lineY + mc.fontRendererObj.FONT_HEIGHT, bodyColor);

            mc.fontRendererObj.drawString(formattedPlayerName, getPosition().getX(), lineY, 553648127);
            mc.fontRendererObj.drawString(scoreString, xEnd - mc.fontRendererObj.getStringWidth(scoreString), lineY, 553648127);
        }

        String objectiveDisplayName = objective.getDisplayName();

        drawRect(getPosition().getX() - 2, getPosition().getY() - (list.size() * mc.fontRendererObj.FONT_HEIGHT), xEnd, getPosition().getY() - (list.size() - 1) * mc.fontRendererObj.FONT_HEIGHT, headerColor);

        mc.fontRendererObj.drawString(objectiveDisplayName, getPosition().getX() + stringWidth / 2 - mc.fontRendererObj.getStringWidth(objectiveDisplayName) / 2, getPosition().getY() - (list.size() * mc.fontRendererObj.FONT_HEIGHT), 553648127);
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
