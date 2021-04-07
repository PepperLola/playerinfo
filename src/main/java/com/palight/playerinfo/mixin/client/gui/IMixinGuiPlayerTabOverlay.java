package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiPlayerTabOverlay.class)
public interface IMixinGuiPlayerTabOverlay {
    @Invoker
    void callDrawScoreboardValues(ScoreObjective p_drawScoreboardValues_1_, int p_drawScoreboardValues_2_, String p_drawScoreboardValues_3_, int p_drawScoreboardValues_4_, int p_drawScoreboardValues_5_, NetworkPlayerInfo p_drawScoreboardValues_6_);
}
