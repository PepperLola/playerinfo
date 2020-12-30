package com.palight.playerinfo.mixin.gui;

import com.palight.playerinfo.PlayerInfo;
import com.palight.playerinfo.gui.ingame.widgets.impl.ScoreboardWidget;
import com.palight.playerinfo.modules.impl.gui.PumpkinMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui {
    @Final
    @Shadow
    protected static ResourceLocation pumpkinBlurTexPath;

    @Final
    @Shadow
    protected Minecraft mc;

    /**
     * @author palight
     * @reason Remove pumpkin overlay
     */
    @Overwrite
    protected void renderPumpkinOverlay(ScaledResolution res) {
        PumpkinMod pumpkinMod = (PumpkinMod) PlayerInfo.getModules().get("pumpkinOverlay");
        if (!pumpkinMod.isEnabled()) {
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableAlpha();
            this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
            Tessellator lvt_2_1_ = Tessellator.getInstance();
            WorldRenderer lvt_3_1_ = lvt_2_1_.getWorldRenderer();
            lvt_3_1_.begin(7, DefaultVertexFormats.POSITION_TEX);
            lvt_3_1_.pos(0.0D, res.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
            lvt_3_1_.pos(res.getScaledWidth(), res.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
            lvt_3_1_.pos(res.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            lvt_3_1_.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            lvt_2_1_.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * @author palight
     * @reason Render custom scoreboard
     */
    @Overwrite
    protected void renderScoreboard(ScoreObjective objective, ScaledResolution resolution) {
        ((ScoreboardWidget) PlayerInfo.getModules().get("scoreboard").getWidget()).render(objective, resolution);
    }
}
