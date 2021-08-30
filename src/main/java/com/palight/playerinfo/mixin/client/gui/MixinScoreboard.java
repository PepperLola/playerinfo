package com.palight.playerinfo.mixin.client.gui;

import net.minecraft.scoreboard.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Mixin(Scoreboard.class)
public class MixinScoreboard {
    @Shadow @Final
    private Map<String, ScoreObjective> scoreObjectives;
    @Shadow @Final
    private Map<IScoreObjectiveCriteria, List<ScoreObjective>> scoreObjectiveCriterias;
    @Shadow @Final
    private Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives;
    @Shadow @Final
    private Map<String, ScorePlayerTeam> teams;
    @Shadow @Final
    private Map<String, ScorePlayerTeam> teamMemberships;


    /**
     * @author palight
     * @reason Fix NPE
     */
    @Overwrite
    public void removeTeam(ScorePlayerTeam team) {
        if (team == null) return;

        if (team.getRegisteredName() != null) {
            this.teams.remove(team.getRegisteredName());
        }

        for (String membership : team.getMembershipCollection()) {
            this.teamMemberships.remove(membership);
        }
    }

    /**
     * @author palight
     * @reason Fix another NPE
     */
    @Overwrite
    public void removeObjective(ScoreObjective objective) {
        if (objective == null) return;

        if (objective.getName() != null) {
            this.scoreObjectives.remove(objective.getName());
        }

        for(int i = 0; i < 19; ++i) {
            if (((IMixinScoreboard) this).callGetObjectiveInDisplaySlot(i) == objective) {
                ((IMixinScoreboard) this).callSetObjectiveInDisplaySlot(i, null);
            }
        }

        List<ScoreObjective> objectives = this.scoreObjectiveCriterias.get(objective.getCriteria());
        if (objectives != null) {
            objectives.remove(objective);
        }

        for (Map<ScoreObjective, Score> scoreObjectiveScoreMap : this.entitiesScoreObjectives.values()) {
            scoreObjectiveScoreMap.remove(objective);
        }

        ((IMixinScoreboard) this).callOnScoreObjectiveRemoved(objective);
    }
}
