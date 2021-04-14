package com.palight.playerinfo.gui.ingame.widgets.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.events.ScoreboardTitleChangeEvent;
import com.palight.playerinfo.gui.ingame.widgets.GuiIngameWidget;
import com.palight.playerinfo.modules.impl.gui.ScoreboardMod;
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
import java.util.stream.Collectors;

public class ScoreboardWidget extends GuiIngameWidget {

    private Minecraft mc;
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

        if (scoreObjective != null && objective != null) {
            if (!ColorUtil.stripColor(scoreObjective.getDisplayName()).equals(ColorUtil.stripColor(objective.getDisplayName()))) {
                MinecraftForge.EVENT_BUS.post(new ScoreboardTitleChangeEvent(scoreObjective.getDisplayName(), objective.getDisplayName()));
            }
        }
        scoreObjective = objective;

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

        this.getPosition().setX(resolution.getScaledWidth() - this.width - 3);
        this.getPosition().setY(resolution.getScaledHeight() / 2 - this.height / 2);

        int scoreboardX = this.getPosition().getX() + module.offsetX;
        int scoreboardY = this.getPosition().getY() + module.offsetY;

        this.width = stringWidth + 2;

        int xEnd = scoreboardX + this.width;

        for (int i = 0; i < list.size(); i++) {
            Score score = list.get(i);

            ScorePlayerTeam teamScore = scoreboard.getPlayersTeam(score.getPlayerName());

            String formattedPlayerName = ScorePlayerTeam.formatPlayerName(teamScore, score.getPlayerName());
            String scoreString = !module.isEnabled() || module.scoreboardNumbersEnabled ? EnumChatFormatting.RED.toString() + score.getScorePoints() : "";

            int lineY = scoreboardY + ((list.size() - i) * mc.fontRendererObj.FONT_HEIGHT);

            drawRect(scoreboardX - 2, lineY, xEnd, lineY + mc.fontRendererObj.FONT_HEIGHT, module.scoreboardBodyColor);

            mc.fontRendererObj.drawString(formattedPlayerName, scoreboardX, lineY, 553648127);
            mc.fontRendererObj.drawString(scoreString, xEnd - mc.fontRendererObj.getStringWidth(scoreString), lineY, 553648127);
        }

        String objectiveDisplayName = objective.getDisplayName();

        drawRect(scoreboardX - 2, scoreboardY, xEnd, scoreboardY + mc.fontRendererObj.FONT_HEIGHT, module.scoreboardHeaderColor);

        mc.fontRendererObj.drawString(objectiveDisplayName, scoreboardX + stringWidth / 2 - mc.fontRendererObj.getStringWidth(objectiveDisplayName) / 2, scoreboardY, 553648127);
    }

    public void resetPosition() {
        if (module == null) {
            module = ((ScoreboardMod) PlayerInfo.getModules().get("scoreboard"));
        }
        module.offsetX = 0;
        module.offsetY = 0;
    }
}
