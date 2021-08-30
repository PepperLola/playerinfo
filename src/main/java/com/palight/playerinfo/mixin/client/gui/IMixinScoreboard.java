package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Scoreboard.class)
public interface IMixinScoreboard {
    @Invoker ScoreObjective callGetObjectiveInDisplaySlot(int slot);
    @Invoker void callSetObjectiveInDisplaySlot(int slot, ScoreObjective objective);
    @Invoker void callOnScoreObjectiveRemoved(ScoreObjective objective);
}
