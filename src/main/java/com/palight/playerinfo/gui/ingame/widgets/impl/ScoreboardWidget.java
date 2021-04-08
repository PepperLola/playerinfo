package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.events.ScoreboardTitleChangeEvent;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.gui.ScoreboardMod;
import com.palight.playerinfo.util.ColorUtil;
import com.palight.playerinfo.util.NumberUtil;
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
import java.util.stream.Collectors;

public class ScoreboardWidget extends GuiIngameWidget {

    private Minecraft mc;
    private int defaultX = -1;
    private int defaultY = -1;
    private int lastScaledWidth = -1;
    private int lastScaledHeight = -1;
    private double lastXDiff = 0;
    private double lastYDiff = 0;
    private int headerColor = 1610612736;
    private int bodyColor = 1342177280;
    public ScoreObjective scoreObjective;

    private ScoreboardMod module;

    public ScoreboardWidget() {
        super(-1, -1, 100, 100);
        this.movable = true;
    }

    public void render(ScoreObjective objective, ScaledResolution resolution) {
        if (module == null) {
            module = ((ScoreboardMod) PlayerInfo.getModules().get("scoreboard"));
        }
        if (module.isEnabled() && !module.scoreboardEnabled) return;

        if (mc == null) {
            mc = Minecraft.getMinecraft();
        }

        if (lastScaledWidth == -1) {
            lastScaledWidth = resolution.getScaledWidth();
        }

        if (lastScaledHeight == -1) {
            lastScaledHeight = resolution.getScaledHeight();
        }

        if (scoreObjective != null && objective != null) {
            if (!ColorUtil.stripColor(scoreObjective.getDisplayName()).equals(ColorUtil.stripColor(objective.getDisplayName()))) {
                MinecraftForge.EVENT_BUS.post(new ScoreboardTitleChangeEvent(scoreObjective.getDisplayName(), objective.getDisplayName()));
            }
        }
        scoreObjective = objective;

        headerColor = module.scoreboardHeaderColor;
        bodyColor = module.scoreboardBodyColor;

        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> sortedScores = scoreboard.getSortedScores(objective);

        ArrayList<Score> filteredList = sortedScores.stream().filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#")).collect(Collectors.toCollection(Lists::newArrayList));

        ArrayList<Score> list;
        if (filteredList.size() > 15) {
            list = Lists.newArrayList(Iterables.skip(filteredList, sortedScores.size() - 15));
        } else {
            list = filteredList;
        }

        int stringWidth = mc.fontRendererObj.getStringWidth(objective.getDisplayName());

        String displayString;
        for (Iterator<Score> iterator = list.iterator(); iterator.hasNext(); stringWidth = Math.max(stringWidth, mc.fontRendererObj.getStringWidth(displayString))) {
            Score score = iterator.next();
            ScorePlayerTeam teamScore = scoreboard.getPlayersTeam(score.getPlayerName());

            displayString = ScorePlayerTeam.formatPlayerName(teamScore, score.getPlayerName());

            if (!module.isEnabled() || module.scoreboardNumbersEnabled) {
                displayString += ": " + EnumChatFormatting.RED + score.getScorePoints();
            }
        }

        this.height = (list.size() + 1) * mc.fontRendererObj.FONT_HEIGHT;

        int padding = 3;

        if (defaultX == -1) {
            defaultX = resolution.getScaledWidth() - stringWidth - padding;
        }

        if (defaultY == -1) {
            defaultY = (resolution.getScaledHeight() - this.height) / 2;
        }

        if (resolution.getScaledWidth() != lastScaledWidth) {
            defaultX = resolution.getScaledWidth() - stringWidth - padding;
            this.getPosition().setX(NumberUtil.clamp(defaultX + (int) (lastXDiff * resolution.getScaledWidth_double()), 0, resolution.getScaledWidth()));
            lastScaledWidth = resolution.getScaledWidth();
        }

        if (resolution.getScaledHeight() != lastScaledHeight) {
            defaultY = (resolution.getScaledHeight() - this.height) / 2;
            this.getPosition().setY(NumberUtil.clamp(defaultY + (int) (lastYDiff * resolution.getScaledHeight_double()), 0, resolution.getScaledHeight()));
            lastScaledHeight = resolution.getScaledHeight();
        }

        if (this.getPosition().getY() == -1) {
            getPosition().setY(defaultY);
        }
        if (this.getPosition().getX() == -1) {
            getPosition().setX(defaultX);
        }

        lastXDiff = (this.getPosition().getX() - defaultX) / resolution.getScaledWidth_double();
        lastYDiff = (this.getPosition().getY() - defaultY) / resolution.getScaledHeight_double();

        this.width = stringWidth + 2;

        int xEnd = getPosition().getX() + this.width;

        for (int i = 0; i < list.size(); i++) {
            Score score = list.get(i);

            ScorePlayerTeam teamScore = scoreboard.getPlayersTeam(score.getPlayerName());

            String formattedPlayerName = ScorePlayerTeam.formatPlayerName(teamScore, score.getPlayerName());
            String scoreString = !module.isEnabled() || module.scoreboardNumbersEnabled ? EnumChatFormatting.RED.toString() + score.getScorePoints() : "";

            int lineY = getPosition().getY() + ((list.size() - i) * mc.fontRendererObj.FONT_HEIGHT);

            drawRect(getPosition().getX() - 2, lineY, xEnd, lineY + mc.fontRendererObj.FONT_HEIGHT, bodyColor);

            mc.fontRendererObj.drawString(formattedPlayerName, getPosition().getX(), lineY, 553648127);
            mc.fontRendererObj.drawString(scoreString, xEnd - mc.fontRendererObj.getStringWidth(scoreString), lineY, 553648127);
        }

        String objectiveDisplayName = objective.getDisplayName();

        drawRect(getPosition().getX() - 2, getPosition().getY(), xEnd, getPosition().getY() + mc.fontRendererObj.FONT_HEIGHT, headerColor);

        mc.fontRendererObj.drawString(objectiveDisplayName, getPosition().getX() + stringWidth / 2 - mc.fontRendererObj.getStringWidth(objectiveDisplayName) / 2, getPosition().getY(), 553648127);
    }

    public void resetPosition() {
        this.getPosition().setX(-1);
        this.getPosition().setY(-1);
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
