package com.palight.playerinfo.mixin.render;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.modules.impl.gui.DisplayTweaksMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T> {

    private DisplayTweaksMod module;

    protected MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    /**
     * @reason Render own name
     * @author palight
     */
    @Overwrite
    public boolean canRenderName(T entity) {
        if (module == null) {
            module = (DisplayTweaksMod) PlayerInfo.getModules().get("displayTweaks");
        }
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
        if (entity instanceof EntityPlayer && (entity != entityplayersp || module.renderOwnName)) {
            Team team = entity.getTeam();
            Team team1 = entityplayersp.getTeam();
            if (team != null) {
                Team.EnumVisible team$enumvisible = team.getNameTagVisibility();
                switch(team$enumvisible) {
                    case NEVER:
                        return false;
                    case HIDE_FOR_OTHER_TEAMS:
                        return team1 == null || team.isSameTeam(team1);
                    case HIDE_FOR_OWN_TEAM:
                        return team1 == null || !team.isSameTeam(team1);
                    case ALWAYS:
                    default:
                        return true;
                }
            }
        }

        return Minecraft.isGuiEnabled() && entity != this.renderManager.livingPlayer && !entity.isInvisibleToPlayer(entityplayersp) && entity.riddenByEntity == null;
    }
}
